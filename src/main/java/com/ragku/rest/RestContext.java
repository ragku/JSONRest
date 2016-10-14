package com.ragku.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.ragku.rest.annotation.RestRequest;
import com.ragku.rest.utils.ClassScanUtil;
import com.ragku.rest.utils.ParameterUtil;

public class RestContext {
	
	private static final Log log = LogFactory.getLog(RestContext.class);
	
	public static final RestContext rc = new RestContext();
	
	private Map<String, Object> beanPool = new HashMap<String, Object>();
	
	private Map<String, RouteInfo> routePool = new HashMap<String, RouteInfo>();
	
	private void initRoute(String key, Class<?> clazz, Method m) throws Exception {
		if(routePool.containsKey(key)) {
			throw new Exception("route " + key + " duplicate");
		}
		RouteInfo ri = new RouteInfo();
		if(beanPool.containsKey(clazz.getName())) {
			ri.obj = beanPool.get(clazz.getName());
		} else {
			ri.obj = clazz.newInstance();
			beanPool.put(clazz.getName(), ri.obj);
		}
		ri.method = m;
		ri.paramNames = ParameterUtil.getMethodParameterNamesByAsm4(clazz, m);
		log.info(key + " " + clazz.getSimpleName() + "." + ri.method.getName() + " " + Arrays.toString(ri.paramNames));
		routePool.put(key, ri);
	}
	
	private Object invoke(final String key, final JSONObject args) throws Exception {
		RouteInfo ri = routePool.get(key);
		if(null == ri) {
			throw new RestException(404, "no route " + key);
		}
		if(null == ri.paramNames || ri.paramNames.length == 0) {
			return ri.method.invoke(ri.obj);
		}
		Object[] params = new Object[ri.paramNames.length];
		for(int i = 0; i < ri.paramNames.length; i++) {
			Class<?> clazz = ri.method.getParameterTypes()[i];
			if(args.containsKey(ri.paramNames[i])) {
				try {
					params[i] = args.getObject(ri.paramNames[i], clazz);
				} catch (Exception e) {
					throw new RestException(402, e.getMessage());
				}
				
			} else if(!args.isEmpty()) {
				params[i] = args.toJavaObject(clazz);
			}
		}
		return ri.method.invoke(ri.obj, params);
	}
	
	private String getBody(HttpServletRequest requ) {
		StringBuilder body = new StringBuilder("");
		String line = null;
		try (BufferedReader br = requ.getReader()){
			while((line = br.readLine()) != null) {
				body.append(line);
			}
		} catch (IOException e) {
			log.error(requ.getServletPath() + " read body error", e);
		}
		return body.toString();
	}
	
	public Object Handle(HttpServletRequest requ) throws Exception {
		String path = requ.getMethod().toUpperCase() + ":" + requ.getServletPath();
		JSONObject params = new JSONObject();
		for (Enumeration<String> e = requ.getParameterNames(); e.hasMoreElements();) {
			String key = e.nextElement();
			params.put(key, requ.getParameter(key));
		}
		JSONObject body = JSONObject.parseObject(getBody(requ));
		if(null != body) {
			params.putAll(body);
		}
		log.info(path + " " + params.toJSONString());
		return invoke(path, params);
	}
	
	public synchronized void init(String packageName) throws Exception {
		packageName = null ==  packageName ? "com" : packageName;
		for(Class<?> clazz : ClassScanUtil.listClasses(packageName)) {
			for(Method m : clazz.getMethods()) {
				for(Annotation a : m.getAnnotations()) {
					RestRequest rr = a.annotationType().getAnnotation(RestRequest.class);
					if(null != rr) {
						String key = rr.value() + ":" + a.annotationType().getMethod("value").invoke(a);
						initRoute(key, clazz, m);
					}
				}
			}
		}
	}
	
	private class RouteInfo {
		private Object obj;
		private Method method;
		private String[] paramNames;
	}
	
}

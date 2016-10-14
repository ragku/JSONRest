package com.ragku.rest;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ragku.rest.utils.ClassScanUtil;
import com.ragku.rest.utils.ParameterUtil;

public class WebContext {
	
	private static final Log log = LogFactory.getLog(WebContext.class);
	
	public static final WebContext wc = new WebContext();
	
	private boolean inited = false;
	
	private Map<String, Object> beanPool = new HashMap<String, Object>();
	
	private Map<String, RouteInfo> routePool = new HashMap<String, RouteInfo>();
	
	private void addMethod(String key, Class<?> clazz, Method m) throws Exception {
		if(routePool.containsKey(key)) {
			throw new Exception("route " + key + " duplicate");
		}
		RouteInfo ri = new RouteInfo();
		if(beanPool.containsKey(clazz.getName())) {
			ri.setObj(beanPool.get(clazz.getName()));
		} else {
			Object obj = clazz.newInstance();
			ri.setObj(obj);
			beanPool.put(clazz.getName(), obj);
		}
		ri.setMethod(m);
		String[] paramNames = ParameterUtil.getMethodParameterNamesByAsm4(clazz, m);
		ri.setParamNames(paramNames);
		log.info(key + " " + ri.toString());
		routePool.put(key, ri);
	}
	
	private Object invoke(final String key, final Map<String, Object> args) throws Exception {
		RouteInfo ri = routePool.get(key);
		if(null == ri) {
			throw new RestException(404, "no route " + key);
		}
		if(null == ri.getParamNames() || ri.getParamNames().length == 0) {
			return ri.getMethod().invoke(ri.getObj());
		}
		Object[] params = new Object[ri.getParamNames().length];
		for(int i = 0; i < ri.getParamNames().length; i++) {
			params[i] = args.get(ri.getParamNames()[i]);
		}
		return ri.getMethod().invoke(ri.getObj(), params);
	}
	
	public Object Handle(HttpServletRequest requ) throws Exception {
		String path = requ.getMethod().toUpperCase() + ":" + requ.getServletPath();
		log.info(path);
		Map<String, Object> params = new HashMap<String, Object>();
		for (Enumeration<String> e = requ.getParameterNames(); e.hasMoreElements();) {
			String key = e.nextElement();
			params.put(key, requ.getParameter(key));
		}
		return invoke(path, params);
	}
	
	
	public synchronized void init(String packageName) throws Exception {
		if(inited) {
			return;
		}
		packageName = null ==  packageName ? "com" : packageName;
		Set<Class<?>> classes = ClassScanUtil.listClasses(packageName);
		log.info("scan " + packageName + " " + classes.size());
		for(Class<?> clazz : classes) {
			for(Method m : clazz.getMethods()) {
				PostMapping pm = m.getAnnotation(PostMapping.class);
				if(null != pm) {
					addMethod("POST:" + pm.value(), clazz, m);
				}
				PutMapping putm = m.getAnnotation(PutMapping.class);
				if(null != putm) {
					addMethod("PUT:" + putm.value(), clazz, m);
				}
				DeleteMapping dm = m.getAnnotation(DeleteMapping.class);
				if(null != dm) {
					addMethod("DELETE:" + dm.value(), clazz, m);
				}
				GetMapping gm = m.getAnnotation(GetMapping.class);
				if(null != gm) {
					addMethod("GET:" + gm.value(), clazz, m);
				}
			}
		}
		inited = true;
	}
	
	
	private class RouteInfo {
		
		private Object obj;
		
		private Method method;
		
		private String[] paramNames;

		public Object getObj() {
			return obj;
		}

		public void setObj(Object obj) {
			this.obj = obj;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public String[] getParamNames() {
			return paramNames;
		}

		public void setParamNames(String[] paramNames) {
			this.paramNames = paramNames;
		}

	}
}

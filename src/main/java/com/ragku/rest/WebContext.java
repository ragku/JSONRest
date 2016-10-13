package com.ragku.rest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ragku.rest.utils.ClassScanUtil;
import com.ragku.rest.utils.ParameterUtil;

public class WebContext {
	
	private static final Log log = LogFactory.getLog(RestHandle.class);
	
	public static final WebContext wc = new WebContext();
	
	private boolean inited = false;
	
	private Map<String, Object> beanPool = new HashMap<String, Object>();
	
	private Map<String, RouteInfo> routePool = new HashMap<String, RouteInfo>();
	
	public void addClass(String className, Object classInstant) {
		beanPool.put(className, classInstant);
	}
	
	public void addMethod(String key, Class<?> clazz, Method m) throws Exception {
		if(routePool.containsKey(key)) {
			throw new Exception("route" + key + "duplicate");
		}
		RouteInfo ri = new RouteInfo();
		if(beanPool.containsKey(clazz.getName())) {
			ri.setController(beanPool.get(clazz.getName()));
		} else {
			Object obj = clazz.newInstance();
			ri.setController(obj);
			beanPool.put(clazz.getName(), obj);
		}
		ri.setMethod(m);
		String[] paramNames = ParameterUtil.getMethodParameterNamesByAsm4(clazz, m);
		ri.setArgs(paramNames);
		log.info(key + " " + ri.toString());
		routePool.put(key, ri);
	}
	
	public RouteInfo getRoute(String key) {
		return routePool.get(key);
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
}

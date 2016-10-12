package com.ragku.rest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class WebContext {
	
	private Map<String, Object> beanPool = new HashMap<String, Object>();
	
	private Map<String, RouteInfo> rutePool = new HashMap<String, RouteInfo>();
	
	public void addClass(String className, Object classInstant) {
		beanPool.put(className, classInstant);
	}
	
	public void addMethod(String mapper, Object obj, Method m) {
		RouteInfo ri = new RouteInfo();
		ri.setController(obj);
		ri.setMethod(m);
		rutePool.put(mapper, ri);
	}
	
	public Map<String, RouteInfo> getMethod() {
		return rutePool;
	}
}

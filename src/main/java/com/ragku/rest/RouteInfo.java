package com.ragku.rest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class RouteInfo {
	
	private Object invoke;
	
	private Method method;
	
	private String[] args;

	public void setController(Object invoke) {
		this.invoke = invoke;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
	public Object invoke(Map<String, Object> params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object[] objs = new Object[args.length];
		for(int i = 0; i < args.length; i++) {
			objs[i] = params.get(args[i]);
		}
		return method.invoke(invoke, objs);
	}
}

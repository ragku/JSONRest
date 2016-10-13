package com.ragku.rest;

import java.lang.reflect.Method;
import java.util.Arrays;
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
	
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	public Object invoke(Map<String, Object> params) throws Exception {
		if(null == args || args.length == 0) {
			return method.invoke(invoke);
		}
		Object[] objs = new Object[args.length];
		for(int i = 0; i < args.length; i++) {
			objs[i] = params.get(args[i]);
		}
		return method.invoke(invoke, objs);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class:").append(invoke.getClass().getName()).append(";method:").append(method.getName()).append("params:");
		if(null != args) {
			sb.append(Arrays.toString(args));
		}
		return sb.toString();
	}
}

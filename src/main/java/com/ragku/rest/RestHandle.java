package com.ragku.rest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

public class RestHandle {

	private HttpServletRequest requ;

	public RestHandle(HttpServletRequest requ) {
		this.requ = requ;
	}

	public Object Handle() {
		JSONObject json = new JSONObject();
		String path = requ.getMethod().toUpperCase() + ":" + requ.getServletPath();
		RouteInfo ri = WebContext.wc.getRoute(path);
		if (null == ri) {
			json.put("success", false);
			json.put("data", "no route mapping");
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			for (Enumeration<String> e = requ.getParameterNames(); e.hasMoreElements();) {
				String key = e.nextElement();
				params.put(key, requ.getParameter(key));
			}
			try {
				Object result = ri.invoke(params);
				json.put("data", result);
				json.put("success", true);
			} catch (Exception e) {
				json.put("success", false);
				json.put("data", e.getMessage());
			}
		}
		return json;
	}
}

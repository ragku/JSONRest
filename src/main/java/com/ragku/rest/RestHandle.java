package com.ragku.rest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RestHandle {
	
	private static final Log log = LogFactory.getLog(RestHandle.class);

	private HttpServletRequest requ;

	public RestHandle(HttpServletRequest requ) {
		this.requ = requ;
	}

	public Object Handle() throws Exception {
		String path = requ.getMethod().toUpperCase() + ":" + requ.getServletPath();
		log.info(path);
		RouteInfo ri = WebContext.wc.getRoute(path);
		if (null == ri) {
			throw new RestException(404, requ.getServletPath());
		}
		Map<String, Object> params = new HashMap<String, Object>();
		for (Enumeration<String> e = requ.getParameterNames(); e.hasMoreElements();) {
			String key = e.nextElement();
			params.put(key, requ.getParameter(key));
		}
		return ri.invoke(params);
	}
}

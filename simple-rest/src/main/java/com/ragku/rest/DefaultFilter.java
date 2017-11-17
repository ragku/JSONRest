package com.ragku.rest;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = { "/*" }, filterName = "defaultFilter", asyncSupported = true)
public class DefaultFilter extends AbstractFilter {

	@Override
	public String getPackageName() {
		return "com.ragku";
	}

	@Override
	public void filter(HttpServletRequest requ, HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		resp.setHeader("Access-Control-Max-Age", "3600");
		resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
		resp.setContentType("application/json; charset=utf-8");
		resp.setHeader("pragma", "no-cache");
		resp.setHeader("cache-control", "no-cache");
	}
}

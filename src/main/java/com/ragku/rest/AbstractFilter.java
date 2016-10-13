package com.ragku.rest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractFilter implements Filter {
	
	abstract String getPackageName();
	
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			WebContext.wc.init(getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest requ = (HttpServletRequest) request;
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		resp.setHeader("Access-Control-Max-Age", "3600");
		resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
		resp.setContentType("application/json; charset=utf-8");
		resp.setHeader("pragma", "no-cache");
		resp.setHeader("cache-control", "no-cache");
		Object obj = new RestHandle(requ).Handle();
		resp.getWriter().print(JSONObject.toJSON(obj));
	}

	public void destroy() {
	}

}

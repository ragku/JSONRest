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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractFilter implements Filter {

	private static final Log log = LogFactory.getLog(Filter.class);

	public abstract String getPackageName();
	
	public abstract void filter(final HttpServletRequest requ, final HttpServletResponse resp);
	
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			log.info("init route from package: " + getPackageName());
			long start = System.currentTimeMillis();
			RestContext.rc.init(getPackageName());
			log.info("init route used time: " + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			log.error("init route error：", e);
			throw new ServletException(e.getMessage());
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest requ = (HttpServletRequest) request;
		try{
			filter(requ, resp);
			Object obj = RestContext.rc.Handle(requ);
			if(null != obj) {
				resp.getWriter().print(JSONObject.toJSON(obj));
			}
		} catch(RestException e) {
			resp.sendError(e.getHttpStatus(), e.getMessage());
		} catch (Exception e) {
			log.error("服务调用错误", e);
			resp.sendError(500, "serve error");
		}
		
	}

	public void destroy() {
	}

}

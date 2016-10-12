package com.ragku.rest;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ragku.rest.annotation.Controller;
import com.ragku.rest.annotation.DeleteMapping;
import com.ragku.rest.annotation.GetMapping;
import com.ragku.rest.annotation.PostMapping;
import com.ragku.rest.annotation.PutMapping;
import com.ragku.rest.util.ClassScanUtil;
import com.ragku.rest.util.MethodUtil;

public class RestFilter implements Filter {
	
	private static final WebContext wc = new WebContext();
	
	private static final void initController() throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
		Set<Class<?>> classes = ClassScanUtil.listClasses("com.ragku");
		for(Class<?> a : classes) {
			if(null == a.getAnnotation(Controller.class)) {
				continue;
			}
			wc.addClass(a.getName(), a);
			for(Method m : a.getMethods()) {
				String[] paramNames = MethodUtil.getMethodParamNames(m);
				
				PostMapping pm = m.getAnnotation(PostMapping.class);
				if(null != pm) {
					wc.addMethod("POST:" + pm.value(), a.newInstance(), m, paramNames);
				}
				PutMapping putm = m.getAnnotation(PutMapping.class);
				if(null != putm) {
					wc.addMethod("PUT:" + putm.value(), a.newInstance(), m, paramNames);
				}
				DeleteMapping dm = m.getAnnotation(DeleteMapping.class);
				if(null != dm) {
					wc.addMethod("DELETE:" + dm.value(), a.newInstance(), m, paramNames);
				}
				GetMapping gm = m.getAnnotation(GetMapping.class);
				if(null != gm) {
					wc.addMethod("GET:" + gm.value(), a.newInstance(), m, paramNames);
				}
			}
		}
	}
	
	static {
		try {
			initController();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init(FilterConfig filterConfig) throws ServletException {
		
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
		String path = requ.getServletPath();
		RouteInfo ri = wc.getRoute(path);
		if(null != ri) {
		}
		resp.getWriter().print("{\"data\":\"Hello World!\",\"path\":\""+ path+"\"}");
	}

	public void destroy() {
	}

}

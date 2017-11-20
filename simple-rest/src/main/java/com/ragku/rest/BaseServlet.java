package com.ragku.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

public class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(BaseServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
        resp.setContentType("application/json; charset=utf-8");
        resp.setHeader("pragma", "no-cache");
        resp.setHeader("cache-control", "no-cache");
        try {
            Object obj = RestContext.rc.Handle(req);
            if (null != obj) {
                resp.getWriter().print(JSONObject.toJSON(obj));
                resp.getWriter().flush();
                resp.getWriter().close();
            }
        } catch (RestException e) {
            resp.sendError(e.getHttpStatus(), e.getMessage());
        } catch (Exception e) {
            log.error("服务调用错误", e);
            resp.sendError(500, "serve error");
        }
    }
}

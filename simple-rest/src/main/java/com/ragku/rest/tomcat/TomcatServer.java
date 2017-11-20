package com.ragku.rest.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import com.ragku.rest.BaseServlet;
import com.ragku.rest.RestContext;

public class TomcatServer {
    static final String docBase = "/tmp/tomcat";

    /**
     * 启动服务
     * @param controllerPackage　controller所在包名，如"com.ragku.api"
     * @param port 服务端口
     * @throws Exception　启动异常
     */
    public static void run(String controllerPackage, int port) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(docBase);
        tomcat.getHost().setAutoDeploy(false);

        Context rootCtx = tomcat.addContext("", docBase);
        Tomcat.addServlet(rootCtx, "baseServlet", new BaseServlet());
        rootCtx.addServletMappingDecoded("/*", "baseServlet");
        
        tomcat.start();
        
        RestContext.rc.init(controllerPackage);
        tomcat.getServer().await();
    }

}

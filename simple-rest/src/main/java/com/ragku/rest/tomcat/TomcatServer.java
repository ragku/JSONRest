package com.ragku.rest.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import com.ragku.rest.BaseServlet;
import com.ragku.rest.RestContext;

public class TomcatServer {
    static final String baseDir = "/tmp/tomcat";

    public static void run(String scanPackage, int port) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.setBaseDir(baseDir);
        tomcat.getHost().setAutoDeploy(false);

        Context rootCtx = tomcat.addContext("", baseDir);
        Tomcat.addServlet(rootCtx, "baseServlet", new BaseServlet());
        rootCtx.addServletMappingDecoded("/*", "baseServlet");
        
        tomcat.start();
        
        RestContext.rc.init(scanPackage);
        tomcat.getServer().await();
    }

}

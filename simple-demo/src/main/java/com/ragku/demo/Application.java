package com.ragku.demo;

import com.ragku.rest.tomcat.TomcatServer;

public class Application {

    public static void main(String[] args) throws Exception {
        TomcatServer.run("com.ragku.demo", 8080);
    }
}

package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.startup.Tomcat;
import org.example.internal.Router;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8000);

        Router router = new Router();
        router.searchForControllers();

        // Serve from the webapp folder
        //  String webappDir = new File("app/src/main/webapp").getAbsolutePath();
//        var ctx = tomcat.addWebapp("", webappDir);
        var ctx = tomcat.addContext("", System.getProperty("java.io.tmpdir"));

        Tomcat.addServlet(ctx, "RootForward", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                var path = req.getRequestURI();
                var route = router.findGetRoutesByPath(path);

                if (route != null){
                    var instance = router.container.get(route.className);
                    try{
                        var value = route.method.invoke(instance);
                        resp.getWriter().println(value);
                    }catch (Exception e){
                        resp.getWriter().println("Internal Server Error: " + e.getMessage());
                    }
                }else{
                    resp.getWriter().println("Not found");
                }
            }
        });
        ctx.addServletMappingDecoded("/", "RootForward");

        // Must create the connector
        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }
}
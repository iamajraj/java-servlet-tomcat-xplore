package org.example.internal;

import org.example.annotations.GET;
import org.example.annotations.RestController;
import org.reflections.Reflections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Router {
    public ArrayList<Route> getRoutes;
    public Map<String, Object> container;

    public Router(){
        this.getRoutes = new ArrayList<>();
        this.container = new HashMap<>();
    }

    public void searchForControllers(){
        Reflections reflections = new Reflections("org.example");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(RestController.class);

        annotated.forEach(clazz -> {
            // add the instance of this class to container
            try {
                var instance = clazz.getDeclaredConstructor().newInstance();
                container.put(clazz.getName(), instance);
            } catch (Exception ignored){}

            var basePath = clazz.getAnnotation(RestController.class).value();
            var methods = clazz.getDeclaredMethods();

            for (var method : methods){
                if (method.isAnnotationPresent(GET.class)){
                    var methodPath = method.getAnnotation(GET.class).value();
                    var concatPath = basePath + methodPath;
                    // remove ending slash
                    concatPath = concatPath.replaceFirst("/$", "");

                    Route route = new Route(concatPath, clazz.getName(), method);
                    this.getRoutes.add(route);
                }
            }
        });
    }

    public Route findGetRoutesByPath(String path){
        for (var route : this.getRoutes){
            if (route.path.equals(path)){
                return route;
            }
        }

        return null;
    }
}

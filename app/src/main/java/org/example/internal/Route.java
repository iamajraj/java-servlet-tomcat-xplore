package org.example.internal;

import java.lang.reflect.Method;

public class Route {
    public String path;
    public String className;
    public Method method;

    public Route(String path, String className, Method method) {
        this.path = path;
        this.className = className;
        this.method = method;
    }
}

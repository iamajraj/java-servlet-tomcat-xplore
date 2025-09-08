package org.example.controller;

import org.example.annotations.GET;
import org.example.annotations.RestController;

import java.util.List;

@RestController("/hello")
public class HelloController {
    @GET("/")
    public String index(){
        return "Index";
    }

    @GET("/world")
    public List<String> world(){
        return List.of("a", "b", "c");
    }
}

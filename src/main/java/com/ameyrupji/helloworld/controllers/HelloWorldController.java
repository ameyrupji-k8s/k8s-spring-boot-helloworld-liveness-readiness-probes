package com.ameyrupji.helloworld.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value= "/helloworld")
public class HelloWorldController {

    @GetMapping(value = "/")
    public String get() {
        return "Hello World!";
    }
}

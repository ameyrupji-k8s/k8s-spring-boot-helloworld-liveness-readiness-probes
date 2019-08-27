package com.ameyrupji.helloworld.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value= "/health")
public class HealthController {

    @GetMapping(value = "/")
    public ResponseEntity get() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
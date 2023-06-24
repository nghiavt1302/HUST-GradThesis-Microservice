package com.nghiavt.productservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    Environment env;

    @GetMapping
    public String test() {
        return "GET test" + env.getProperty("local.server.port");
    }
    @PostMapping
    public String test1() {
        System.out.println("123g");
        return "POST test" + env.getProperty("local.server.port");
    }
}

package com.bankingsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping({"/", "/index", "/home"})
    public String index() {
        return "forward:/index.html";
    }
}



package com.bellator.bellator_barbearia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/app")
    public String index() {
        return "forward:/index.html";
    }
}
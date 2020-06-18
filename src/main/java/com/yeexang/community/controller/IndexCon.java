package com.yeexang.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexCon {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}

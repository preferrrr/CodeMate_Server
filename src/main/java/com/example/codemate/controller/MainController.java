package com.example.codemate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/main")
public class MainController {

    @GetMapping(value = "")
    public String main(){

        return "index";
    }

}

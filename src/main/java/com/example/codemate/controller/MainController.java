package com.example.codemate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/main")
public class MainController {

    @GetMapping(value = "")
    public String main(){

        return "index";
    }

}

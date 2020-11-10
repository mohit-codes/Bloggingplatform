package com.bloggingplatform.minorproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class HomeController {
    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Blogging platform");
        return "home";
    }

}

package com.bloggingplatform.minorproject.controller;

import java.security.Principal;

import com.bloggingplatform.minorproject.dao.UserRepository;
import com.bloggingplatform.minorproject.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    User user;

    @RequestMapping("/feed")
    public String feed(Model model, Principal principal) {
        String email = principal.getName();
        user = userRepository.getUserByEmail(email);

        return "normal/feed";
    }

    @RequestMapping("/userblogs")
    public String myblogs(Model model) {

        model.addAttribute("user", user);
        return "normal/userblogs";
    }
}

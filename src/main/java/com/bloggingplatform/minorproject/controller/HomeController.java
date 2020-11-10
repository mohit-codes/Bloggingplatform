package com.bloggingplatform.minorproject.controller;

import com.bloggingplatform.minorproject.dao.UserRepository;
import com.bloggingplatform.minorproject.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        User user = new User();
        user.setFirstName("mohit");
        user.setLastName("kush");
        userRepository.save(user);
        return "working";
    }
}

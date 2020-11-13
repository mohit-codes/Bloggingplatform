package com.bloggingplatform.minorproject.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.bloggingplatform.minorproject.dao.UserRepository;
import com.bloggingplatform.minorproject.entities.User;
import com.bloggingplatform.minorproject.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class HomeController {
    @Autowired
    private UserRepository userRepository;

    // home handler
    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Blogging platform");
        return "home";
    }

    // login handler
    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Blogging platform");
        return "login";
    }

    // signup handler
    @RequestMapping("/register")
    public String register(Model model) {
        // model.addAttribute("title", "Blogging platform");
        model.addAttribute("user", new User());
        return "register";
    }

    // handler for user register
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
            HttpSession session) {

        try {
            System.out.println("OM ------- " + result);
            if (!agreement) {
                System.out.println("You have not agreed the terms and conditions");
                throw new Exception("You have not agreed the terms and conditions");
            }

            // error validation
            if (result.hasErrors()) {

                System.out.println("MOHIT-------ERROR" + result.toString());
                model.addAttribute("user", user); // from data back to fields
                return "register";
            }

            user.setRole("role_user");
            user.setEnabled(true);

            System.out.println("agreement" + agreement);
            System.out.println("User" + user);

            User resultRepo = this.userRepository.save(user);
            System.out.println("Result" + resultRepo);

            model.addAttribute("user", new User());

            session.setAttribute("message", new Message("Successfully Registered !", "alert-success"));

            return "register";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("something went wrong !" + e.getMessage(), "alert-error"));
            return "register";
        }

    }

}

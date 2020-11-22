package com.bloggingplatform.minorproject.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.bloggingplatform.minorproject.dao.QuestionRepository;
import com.bloggingplatform.minorproject.dao.UserRepository;
import com.bloggingplatform.minorproject.entities.User;
import com.bloggingplatform.minorproject.helper.Message;

import com.bloggingplatform.minorproject.entities.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class HomeController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    // qna handler
    @RequestMapping("/qna")
    public String qna(Model model) {

        List<Question> questions = this.questionRepository.findAll();
        model.addAttribute("navTitle", "QnA Section");
        model.addAttribute("questions", questions);
        return "qna";
    }

    // qna-answer handler
    @RequestMapping("/qna/answer/{questionId}")
    public String answers(@PathVariable("questionId") Integer questionId, Model model) {
        Question questionObj = this.questionRepository.findQuestionById(questionId);

        model.addAttribute("navTitle", "Answers");
        model.addAttribute("question", questionObj);
        return "publicViewAnswer";
    }

    // home handler
    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Blogging platform");
        return "home";
    }

    // signup handler
    @RequestMapping("/register")
    public String register(Model model) {
        // model.addAttribute("title", "Blogging platform");
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/login")
    public String customLogin(Model model) {
        model.addAttribute("title", "login");
        return "login";
    }

    @GetMapping("/admin")
    public String adminLogin(Model model) {
        List<User> allUsers = this.userRepository.findAll();

        model.addAttribute("users", allUsers);
        model.addAttribute("title", "ADMIN");
        return "admin/dashboard";
    }

    // qna topic search
    @PostMapping(value = "/qna/search-topic")
    public String qnaSearchByTopic(@ModelAttribute Question tempObj, HttpSession session, Model model) {
        try {
            String topic = tempObj.getTopic();
            List<Question> questionList = this.questionRepository.findAllByTopic(topic);
            // System.out.println(topic);
            model.addAttribute("navTitle", questionList.size() + " Results for " + topic);
            model.addAttribute("questionList", questionList);

        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            // message error
            session.setAttribute("message", new Message("Something went wrong ! Try again..", "danger"));
        }
        return "qnaTopic";
    }

    // handler for user register
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
            HttpSession session) {

        try {

            if (!agreement) {
                System.out.println("You have not agreed the terms and conditions");
                throw new Exception("You have not agreed the terms and conditions");
            }

            // error validation
            if (result.hasErrors()) {

                System.out.println("ERROR" + result.toString());
                model.addAttribute("user", user); // from data back to fields
                return "register";
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
            System.out.println("agreement" + agreement);
            System.out.println("User" + user);

            User resultRepo = this.userRepository.save(user);
            System.out.println("Result" + resultRepo);

            model.addAttribute("user", new User());

            session.setAttribute("message", new Message("Successfully Registered !", "alert-success"));

            return "login";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("something went wrong !" + e.getMessage(), "alert-error"));
            return "register";
        }

    }

}

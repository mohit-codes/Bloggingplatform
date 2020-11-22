package com.bloggingplatform.minorproject.controller;

import java.util.List;

import com.bloggingplatform.minorproject.dao.AnswerRepository;
import com.bloggingplatform.minorproject.dao.BlogRepository;
import com.bloggingplatform.minorproject.dao.QuestionRepository;
import com.bloggingplatform.minorproject.entities.Answer;
import com.bloggingplatform.minorproject.entities.Blog;
import com.bloggingplatform.minorproject.entities.Question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @RequestMapping("/blogs")
    public String blogFetch(Model model) {
        List<Blog> blogs = this.blogRepository.findAll();

        model.addAttribute("blogs", blogs);
        return "admin/blogs";

    }

    @RequestMapping("/answers")
    public String answerFetch(Model model) {
        List<Answer> answers = this.answerRepository.findAll();

        model.addAttribute("answers", answers);
        return "admin/answers";

    }

    @RequestMapping("/questions")
    public String questionFetch(Model model) {
        List<Question> questions = this.questionRepository.findAll();

        model.addAttribute("questions", questions);
        return "admin/questions";

    }
}

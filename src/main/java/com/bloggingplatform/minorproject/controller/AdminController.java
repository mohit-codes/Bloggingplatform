package com.bloggingplatform.minorproject.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.bloggingplatform.minorproject.dao.AnswerRepository;
import com.bloggingplatform.minorproject.dao.BlogRepository;
import com.bloggingplatform.minorproject.dao.QuestionRepository;
import com.bloggingplatform.minorproject.dao.UserRepository;
import com.bloggingplatform.minorproject.entities.Answer;
import com.bloggingplatform.minorproject.entities.Blog;
import com.bloggingplatform.minorproject.entities.Question;
import com.bloggingplatform.minorproject.entities.User;
import com.bloggingplatform.minorproject.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private UserRepository userRepository;

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

    @RequestMapping("blogs/delete-blog/{blogid}")
    public String deleteblog(@PathVariable("blogid") Integer blogid, Model model, HttpSession session) {
        try {

            Blog blog = this.blogRepository.findById(blogid).get();
            User tempUser = this.userRepository.getUserById(blog.getUser().getId());

            tempUser.getBlogs().remove(blog);
            blog.setUser(null);

            this.blogRepository.delete(blog);

            this.userRepository.save(tempUser);
            System.out.println("Deleted");

            // message success.......
            session.setAttribute("message", new Message("blog deleted !", "success"));
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            // message error
            session.setAttribute("message", new Message("Something went wrong ! Try again..", "danger"));
        }
        return "redirect:/admin/blogs";

    }

    @RequestMapping("questions/delete-question/{questionid}")
    public String deletequestion(@PathVariable("questionid") Integer questionid, Model model, HttpSession session) {
        try {

            Question question = this.questionRepository.findById(questionid).get();
            User tempUser = this.userRepository.getUserById(question.getUser().getId());

            tempUser.getQuestions().remove(question);
            question.setUser(null);

            this.questionRepository.delete(question);

            this.userRepository.save(tempUser);
            System.out.println("Deleted");

            // message success.......
            session.setAttribute("message", new Message("question deleted !", "success"));
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            // message error
            session.setAttribute("message", new Message("Something went wrong ! Try again..", "danger"));
        }
        return "redirect:/admin/questions";

    }

}

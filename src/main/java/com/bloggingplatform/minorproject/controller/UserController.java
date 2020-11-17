package com.bloggingplatform.minorproject.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.bloggingplatform.minorproject.dao.BlogRepository;
import com.bloggingplatform.minorproject.dao.QuestionRepository;
import com.bloggingplatform.minorproject.dao.UserRepository;
import com.bloggingplatform.minorproject.entities.Blog;
import com.bloggingplatform.minorproject.entities.Question;
import com.bloggingplatform.minorproject.entities.User;
import com.bloggingplatform.minorproject.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    BlogRepository blogRepository;

    User usercommon;

    @PostMapping("/process-question")
    public String uploadquestion(@ModelAttribute Question question, Principal principal, HttpSession session) {
        try {
            String asker = principal.getName();
            User questionUser = userRepository.getUserByEmail(asker);
            questionUser.getQuestions().add(question);
            question.setUser(questionUser);
            question.setAsker(questionUser.getFirstName() + ' ' + questionUser.getLastName());
            this.userRepository.save(questionUser);
            System.out.println("DATA " + question);

            System.out.println("Added to data base");
            session.setAttribute("message", new Message("Your question is added !", "success"));

        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            // message error
            session.setAttribute("message", new Message("Something went wrong ! Try again..", "danger"));
        }
        return "normal/qna";
    }

    @PostMapping("/process-blog")
    public String uploadblog(@ModelAttribute Blog blog, Principal principal, HttpSession session) {
        try {

            String email = principal.getName();
            User user = this.userRepository.getUserByEmail(email);

            // processing and uploading file..

            // if (file.isEmpty()) {
            // // if the file is empty then try our message
            // System.out.println("File is empty");

            // } else {
            // // file the file to folder and update the name to contact
            // blog.setImage(file.getOriginalFilename());

            // File saveFile = new ClassPathResource("static/images").getFile();

            // Path path = Paths.get(saveFile.getAbsolutePath() + File.separator +
            // file.getOriginalFilename());

            // Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // System.out.println("Image is uploaded");

            // }
            // model.addAttribute("blog",new Blog());
            user.getBlogs().add(blog);

            blog.setUser(user);
            blog.setAuthor(user.getFirstName() + ' ' + user.getLastName());
            this.userRepository.save(user);

            System.out.println("DATA " + blog);

            System.out.println("Added to data base");

            // message success.......
            session.setAttribute("message", new Message("Your Blog is added !", "success"));
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            // message error
            session.setAttribute("message", new Message("Something went wrong ! Try again..", "danger"));
        }
        return "normal/addblog";
    }

    @RequestMapping("/feed")
    public String feed(Model model, Principal principal) {

        String email = principal.getName();

        usercommon = userRepository.getUserByEmail(email);

        List<Blog> blogs = this.blogRepository.findAll();

        model.addAttribute("blogs", blogs);

        return "normal/feed";
    }

    @RequestMapping("/userblogs")
    public String myblogs(Model model, Principal principal) {
        Integer userid = usercommon.getId();
        List<Blog> blogs = this.blogRepository.findByEmail(userid);
        model.addAttribute("blogs", blogs);
        return "normal/userblogs";
    }

    @RequestMapping("/userquestions")
    public String myquestions(Model model, Principal principal) {
        Integer userid = usercommon.getId();
        List<Question> questions = this.questionRepository.findByUserId(userid);
        model.addAttribute("questions", questions);
        return "normal/userquestions";
    }

    @RequestMapping("/profile")
    public String profile(Model model) {

        model.addAttribute("user", usercommon);
        return "normal/profile";
    }

    @RequestMapping("/qna")
    public String qna(Model model) {
        Integer userid = usercommon.getId();
        List<Question> questions = this.questionRepository.findByUserIdNotAsked(userid);

        model.addAttribute("questions", questions);
        return "normal/qna";
    }

    @GetMapping("/add-blog")
    public String openblogForm(Model model) {
        model.addAttribute("title", "Blogging platform");
        model.addAttribute("blog", new Blog());

        return "normal/addblog";
    }
}

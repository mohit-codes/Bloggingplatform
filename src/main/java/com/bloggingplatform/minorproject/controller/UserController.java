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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/feed/{page}")
    public String feed(@PathVariable("page") Integer page, Model model, Principal principal) {

        String email = principal.getName();

        usercommon = userRepository.getUserByEmail(email);

        Pageable pageable = PageRequest.of(page, 8);

        Page<Blog> blogs = this.blogRepository.findAllWithPage(pageable);

        model.addAttribute("navTitle", "Blogs");
        model.addAttribute("currentPage", page);
        model.addAttribute("blogs", blogs);
        model.addAttribute("totalPages", blogs.getTotalPages());

        return "normal/feed";
    }

    @RequestMapping("/userblogs/{page}")
    public String myblogs(@PathVariable("page") Integer page, Model model, Principal principal) {
        Integer userid = usercommon.getId();
        Pageable pageable = PageRequest.of(page, 8);
        Page<Blog> blogs = this.blogRepository.findByEmail(userid, pageable);

        model.addAttribute("navTitle", "Your Blogs");
        model.addAttribute("currentPage", page);
        model.addAttribute("blogs", blogs);
        model.addAttribute("totalPages", blogs.getTotalPages());
        return "normal/userblogs";
    }

    @RequestMapping("/userquestions/{page}")
    public String myquestions(@PathVariable("page") Integer page, Model model, Principal principal) {
        Integer userid = usercommon.getId();
        Pageable pageable = PageRequest.of(page, 8);
        Page<Question> questions = this.questionRepository.findByUserId(userid, pageable);
        model.addAttribute("navTitle", "Asked Question");
        model.addAttribute("currentPage", page);
        model.addAttribute("questions", questions);
        model.addAttribute("totalPages", questions.getTotalPages());
        return "normal/userquestions";
    }

    @RequestMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("navTitle", "Profile");
        model.addAttribute("hide", true);
        model.addAttribute("user", usercommon);
        return "normal/profile";

    }

    // per page = 10
    // current page = 0
    @RequestMapping("/qna/{page}")
    public String qna(@PathVariable("page") Integer page, Model model) {
        Integer userid = usercommon.getId();
        Pageable pageable = PageRequest.of(page, 8);
        Page<Question> questions = this.questionRepository.findByUserIdNotAsked(userid, pageable);

        model.addAttribute("navTitle", "Questions");
        model.addAttribute("questions", questions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questions.getTotalPages());
        return "normal/qna";

    }

    @GetMapping("/qna/answers/{id}")
    public String ans(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("navTitle", "Answer");

        return "normal/answer";

    }

    @GetMapping("/add-blog")
    public String openblogForm(Model model) {
        model.addAttribute("title", "Blogging platform");
        model.addAttribute("blog", new Blog());

        return "normal/addblog";
    }
}

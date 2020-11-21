package com.bloggingplatform.minorproject.controller;

import java.security.Principal;

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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private AnswerRepository answerRepository;

    User usercommon;
    Integer questionId;

    @PostMapping(value = "/process-question")
    public String uploadquestion(@ModelAttribute Question question1, Principal principal, HttpSession session) {
        try {
            String asker = principal.getName();
            User questionUser = userRepository.getUserByEmail(asker);
            questionUser.getQuestions().add(question1);
            String today = java.time.LocalDate.now().toString();

            question1.setDate(today.substring(8) + "-" + today.substring(5, 7) + "-" + today.substring(0, 4));
            question1.setUser(questionUser);
            question1.setAsker(questionUser.getFirstName() + ' ' + questionUser.getLastName());
            this.userRepository.save(questionUser);
            System.out.println("DATA " + question1);

            System.out.println("Added to data base");
            session.setAttribute("message", new Message("Your question is added !", "success"));

        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            // message error
            session.setAttribute("message", new Message("Something went wrong ! Try again..", "danger"));
        }
        return "redirect:/user/userquestions/0";
    }

    @PostMapping(value = "/process-blog")
    public String uploadblog(@ModelAttribute Blog blog, Principal principal, HttpSession session) {
        try {

            String email = principal.getName();
            User user = this.userRepository.getUserByEmail(email);
            user.setBlogCount(user.getBlogCount() + 1);
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
        return "redirect:/user/feed/0";
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

    @RequestMapping("/qna/answer/{id}")
    public String ans(@PathVariable("id") Integer id, Model model) {
        Question question = this.questionRepository.findQuestionById(id);
        questionId = id;
        model.addAttribute("question", question);
        model.addAttribute("navTitle", "Answer");

        return "normal/answer";

    }

    @RequestMapping("/userquestions/answer/{id}")
    public String viewAns(@PathVariable("id") Integer id, Model model) {
        Question question = this.questionRepository.findQuestionById(id);
        questionId = id;
        model.addAttribute("question", question);
        model.addAttribute("navTitle", "Answer");

        return "normal/viewanswer";

    }
    // @GetMapping("/add-blog")
    // public String openblogForm(Model model) {
    // model.addAttribute("title", "Blogging platform");
    // model.addAttribute("blog", new Blog());

    // return "normal/addblog";
    // }

    @PostMapping(value = "/process-answer")
    public String postAnswer(@ModelAttribute Answer answer, Principal principal, HttpSession session) {
        try {

            String email = principal.getName();
            User user = this.userRepository.getUserByEmail(email);
            Question question = this.questionRepository.findQuestionById(questionId);
            answer.setAuthor(user.getFirstName() + " " + user.getLastName());
            String today = java.time.LocalDate.now().toString();

            answer.setDate(today.substring(8) + "-" + today.substring(5, 7) + "-" + today.substring(0, 4));

            question.getAnswers().add(answer);
            this.questionRepository.save(question);

            System.out.println("Added to data base");

            // message success.......
            session.setAttribute("message", new Message("Your answer is submitted !", "success"));
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            // message error
            session.setAttribute("message", new Message("Something went wrong ! Try again..", "danger"));
        }
        return "redirect:/user/qna/answer/" + questionId;
    }

    @RequestMapping("/userblogs/delete-blog/{blogid}")
    public String deleteblog(@PathVariable("blogid") Integer blogid, Principal principal, Model model,
            HttpSession session) {
        try {

            Blog blog = this.blogRepository.findById(blogid).get();
            User tempUser = this.userRepository.getUserByEmail(principal.getName());

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
        return "redirect:/user/userblogs/0";

    }

    @RequestMapping("/userquestions/delete-question/{questionid}")
    public String deletequestion(@PathVariable("questionid") Integer questionid, Principal principal, Model model,
            HttpSession session) {
        try {

            Question question = this.questionRepository.findById(questionid).get();
            User tempUser = this.userRepository.getUserByEmail(principal.getName());

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
        return "redirect:/user/userquestions/0";

    }

    @RequestMapping("/userblogs/edit-blog/{blogid}")
    public String editBlog(@PathVariable("blogid") Integer blogid, Model model) {
        Blog blog = this.blogRepository.findById(blogid).get();
        model.addAttribute("id", blogid);
        model.addAttribute("blog", blog);
        model.addAttribute("navTitle", "Edit Blog");

        return "normal/editblog";

    }

    @PostMapping(value = "/userblogs/process-edit-blog/{blogid}")
    public String editblog(@PathVariable("blogid") Integer blogid, @ModelAttribute Blog blog, Principal principal,
            HttpSession session) {
        try {
            Blog oldblog = this.blogRepository.findById(blogid).get();
            oldblog.setTitle(blog.getTitle());
            oldblog.setTopic(blog.getTopic());
            oldblog.setContent(blog.getContent());
            this.blogRepository.save(oldblog);

            System.out.println("Added to data base");

            // message success.......
            session.setAttribute("message", new Message("Your Blog is updated !", "success"));
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            // message error
            session.setAttribute("message", new Message("Something went wrong ! Try again..", "danger"));
        }
        return "redirect:/user/feed/0";
    }

    @RequestMapping(value = "/like/{blogid}")
    public String blogLikeMethod(@PathVariable("blogid") Integer blogid, HttpSession session) {
        try {
            Blog blog = this.blogRepository.findById(blogid).get();
            blog.setLikes(blog.getLikes() + 1);
            this.blogRepository.save(blog);
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            // message error
            session.setAttribute("message", new Message("Something went wrong ! Try again..", "danger"));
        }
        return "redirect:/user/feed/0";
    }

    @RequestMapping(value = "/answer-like/{answerid}/{type}")
    public String answerLikeMethod(@PathVariable("answerid") Integer answerid, @PathVariable("type") String type,
            HttpSession session) {
        try {
            Answer answer = this.answerRepository.findById(answerid).get();
            answer.setUpvotes(answer.getUpvotes() + 1);
            this.answerRepository.save(answer);

        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            // message error
            session.setAttribute("message", new Message("Something went wrong ! Try again..", "danger"));
        }
        String page;

        if (type.equals("typeA")) {
            page = "qna";
        } else {
            page = "userquestions";
        }
        return "redirect:/user/" + page + "/answer/" + questionId;
    }

    @PostMapping(value = "/profile-update/{id}")
    public String upldateprofile(@PathVariable("id") Integer id, @ModelAttribute User user, HttpSession session) {
        try {
            User userObj = this.userRepository.findById(id).get();

            userObj.setFirstName(user.getFirstName());
            userObj.setLastName(user.getLastName());
            userObj.setAbout(user.getAbout());

            this.userRepository.save(userObj);
            System.out.println("DATA " + userObj.getAbout() + userObj.getFirstName());

            System.out.println("Added to data base");
            session.setAttribute("message", new Message("Details Updated!", "success"));

        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            // message error
            session.setAttribute("message", new Message("Something went wrong ! Try again..", "danger"));
        }
        return "redirect:/user/userblogs/0";
    }

}

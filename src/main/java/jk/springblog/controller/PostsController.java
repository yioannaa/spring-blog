package jk.springblog.controller;


import jk.springblog.model.Comment;
import jk.springblog.model.Post;
import jk.springblog.model.User;
import jk.springblog.model.enums.CategoryEnum;
import jk.springblog.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class PostsController {

    PostsService postsService;
    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }



    private User getUserFromSession(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setId(-1L);
            user.setEmail("Unlogged");
        }

        return user;
    }


    @GetMapping("/")
    public String home(Model model, HttpSession httpSession){
        List<Post> posts = postsService.getAllPosts();
        User user = getUserFromSession(httpSession);

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "posts";
    }


    @GetMapping("/post/{post_id}")
    public String getPost(@PathVariable Long post_id, Model model, HttpSession httpSession){
        model.addAttribute("user", getUserFromSession(httpSession));
        Post post = postsService.getPostById(post_id);
        model.addAttribute("post",post);
        model.addAttribute("comment",new Comment());

        return "selectedpost";
    }

    @PostMapping("/addcomment/{post_id}/{user_id}")
    public String addComment(@ModelAttribute Comment comment,
                             @PathVariable Long post_id,
                             @PathVariable Long user_id){
        postsService.addComment(comment,post_id, user_id);
        return "redirect:/post/" + post_id;
    }

    @GetMapping("/addpost")
    public String addPost(Model model, HttpSession httpSession){
        model.addAttribute("user", getUserFromSession(httpSession));
        model.addAttribute("post",new Post());
        List<CategoryEnum> categories =
                new ArrayList<>(Arrays.asList(CategoryEnum.values()));
        System.out.println(categories);
        model.addAttribute("categories", categories);
        return "addpost";
    }
    @PostMapping("/addpost/{user_id}")
    public String addPost(@ModelAttribute Post post, Model model, @PathVariable Long user_id){
        Post tmp = postsService.savePost(post, user_id);
        return "redirect:/post/" + tmp.getId();
    }

    @DeleteMapping("/delete/{post_id}")
    public String deletePost(@PathVariable Long post_id, HttpSession httpSession, Model model){
        Optional<User> user = Optional.of(getUserFromSession(httpSession));
        model.addAttribute("user", user.get());
        if (user.get().getId() != null) {
            postsService.deleteById(post_id, user.get().getId());
        }
        return "redirect:/";
    }
    @GetMapping("/update/{post_id}")
    public String updatePost(@PathVariable Long post_id, Model model, HttpSession httpSession){
        Optional<User> user = Optional.of(getUserFromSession(httpSession));
        model.addAttribute("user", getUserFromSession(httpSession));

        if (user.get().getId() != null) {
            if (postsService.isOwner(post_id, user.get().getId())) {
                List<CategoryEnum> categories = new ArrayList<>(Arrays.asList(CategoryEnum.values()));
                model.addAttribute("categories", categories);
                Post post = postsService.getPostById(post_id);
                model.addAttribute("post", post);
                return "updatepost";
            }
        }
        return "redirect:/";
    }

    @PutMapping("/update/{post_id}")
    public String updatePost(@PathVariable Long post_id, @ModelAttribute Post post){
        postsService.updatePost(post_id, post);
        return "redirect:/";
    }

}

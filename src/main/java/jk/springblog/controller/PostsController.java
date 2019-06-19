package jk.springblog.controller;


import jk.springblog.model.Post;
import jk.springblog.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PostsController {

    PostsService postsService;
    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }
    @GetMapping("/")
    public String home(Model model){
        List<Post> posts = postsService.getAllPosts();
        model.addAttribute("posts",posts);

        return "posts";
    }

    @GetMapping("/addpost")
    public String addPost(){
        return "addpost";
    }
    
    @GetMapping("/post/{post_id}")
    public String getPost(@PathVariable Long post_id){
        return "selectedpost";
    }
}

package jk.springblog.controller;


import jk.springblog.model.Post;
import jk.springblog.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PostsController {

    PostsService postsService;
    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }
    @GetMapping("/")
    public String home(){
        List<Post> posts = postsService.getAllPosts();
        System.out.println(posts);
        return "posts";
    }

    @GetMapping("/addpost")
}

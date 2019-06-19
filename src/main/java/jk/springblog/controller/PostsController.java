package jk.springblog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostsController {

    public String home(){
        return "posts";
    }
    @GetMapping("/addpost")
    public String addPost(){
        return "addpost";
    }
}

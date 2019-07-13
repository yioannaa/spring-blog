package jk.springblog.controller;


import jk.springblog.model.Comment;
import jk.springblog.model.Post;
import jk.springblog.model.enums.CategoryEnum;
import jk.springblog.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PostsController {
    PostsService postsService;

    @Autowired
    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping("/")
    public String home(Model model, Authentication auth) {
        if (auth != null) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            model.addAttribute("loggedEmail", userDetails.getUsername());
            model.addAttribute("isAdmin", postsService.isAdmin(userDetails));
        }
        List<Post> posts = postsService.getAllPosts();
        Set<CategoryEnum> categories = new HashSet<>();
        for (Post post :posts)
        {
            categories.add(post.getCategory());
        }
        model.addAttribute("categories", categories);
            model.addAttribute("posts", posts);


        return "posts";
    }


//    @GetMapping("/addpost")
//    public String addPost(){
//        return "addpost";
//    }

    @GetMapping("/filter_category{category}")
    public String filterCategories(@PathVariable CategoryEnum category, Model model){
        List<Post>posts = postsService.filterByCategory(category);
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/post/{post_id}")
    public String getPost(@PathVariable Long post_id, Model model, Authentication auth) {
        if (auth != null) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            model.addAttribute("loggedEmail", userDetails.getUsername());
            model.addAttribute("isAdmin", postsService.isAdmin(userDetails));
        }
        Post post = postsService.getPostById(post_id);
        post.setComments(post.getComments()
                .stream()
                .sorted(Comparator.comparing(Comment::getId)
                        .reversed())
                .collect(Collectors.toList()));
        model.addAttribute("post", post);
        model.addAttribute("comment", new Comment());

        return "selectedpost";
    }

    @PostMapping("/addcomment/{post_id}/{user_id}")
    public String addComment(@ModelAttribute Comment comment,
                             @PathVariable Long post_id,
                             Authentication auth) {
        postsService.addComment(comment, post_id, auth);
        return "redirect:/post/" + post_id;
    }

    @GetMapping("/addpost")
    public String addPost(Model model, Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        model.addAttribute("post", new Post());
        List<CategoryEnum> categories =
                new ArrayList<>(Arrays.asList(CategoryEnum.values()));
        System.out.println(categories);
        model.addAttribute("categories", categories);
        return "addpost";
    }

    @PostMapping("/addpost")
    public String addPost(@ModelAttribute Post post, Authentication authentication) {
        UserDetails loggedUserDetails = (UserDetails) authentication.getPrincipal();
        postsService.savePost(post, loggedUserDetails.getUsername());
        return "redirect:/";
    }

    @DeleteMapping("/delete/{post_id}")
    public String deletePost(@PathVariable Long post_id) {
        postsService.deletePost(post_id);
        return "redirect:/";
    }

    @GetMapping("/update/{post_id}")
    public String updatePost(@PathVariable Long post_id, Model model) {
        Post post = postsService.getPostById(post_id);
        model.addAttribute("post", post);
        List<CategoryEnum> categories =
                new ArrayList<>(Arrays.asList(CategoryEnum.values()));
        System.out.println(categories);
        model.addAttribute("categories", categories);
        return "updatepost";
    }

    @PutMapping("/update/{post_id}")
    public String updatePost(@PathVariable Long post_id, @ModelAttribute Post post) {
        postsService.updatePost(post_id, post);
        return "redirect:/";
    }

    @DeleteMapping("/delete_comment/{comment_id}")
    public String deleteComment(@PathVariable Long comment_id) {
        Long post_id = postsService.getPostIdByCommentId(comment_id);
        postsService.deleteComment(comment_id);
        return "redirect:/post/" + post_id;
    }

    @GetMapping("/post_like/{post_id}")
    public String likePost(@PathVariable Long post_id) {
        postsService.likePost(post_id);
        return "redirect:/post/" + post_id;
    }


    @GetMapping("/post_not_like/{post_id}")
    public String notLikePost(@PathVariable Long post_id) {
        postsService.notLikePost(post_id);
        return "redirect:/post/" + post_id;
    }


}



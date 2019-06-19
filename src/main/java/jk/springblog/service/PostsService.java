package jk.springblog.service;


import jk.springblog.model.Post;
import jk.springblog.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostsService {

    PostsRepository postsRepository;
    @Autowired
    public PostsService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }
    public List<Post> getAllPosts(){
        return postsRepository.findAll();
    }
}

package jk.springblog.service;


import jk.springblog.model.Comment;
import jk.springblog.model.Post;
import jk.springblog.model.User;
import jk.springblog.repository.CommentRepository;
import jk.springblog.repository.PostsRepository;
import jk.springblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostsService {

    PostsRepository postsRepository;
    UserRepository userRepository;
    CommentRepository commentRepository;

    @Autowired
    public PostsService(PostsRepository postsRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }


    public List<Post> getAllPosts(){
        return postsRepository.findAll();
    }

    public Post getPostById(Long post_id){
        if(postsRepository.findById(post_id).isPresent()) {
            return postsRepository.getOne(post_id);
        }
        return new Post();
    }

    public void addComment(Comment comment, Long post_id, Long user_id){
        User user = userRepository.getOne(user_id);
        Post post = postsRepository.getOne(post_id);
        comment.setPost(post);
        comment.setUser(user);
        commentRepository.save(comment);
    }

    public Post savePost(Post post, Long user_id){
        post.setUser(userRepository.getOne(user_id)); //póki co user ustawiony na sztywno
        return postsRepository.save(post);
    }

    public void deletePost(Long post_id){
        postsRepository.deleteById(post_id);
    }
    public void updatePost(Long post_id, Post updatedPost){
        Post post = postsRepository.getOne(post_id);
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setCategory(updatedPost.getCategory());
        postsRepository.save(post);
    }
}

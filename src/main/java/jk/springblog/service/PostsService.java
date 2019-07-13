package jk.springblog.service;


import jk.springblog.model.Comment;
import jk.springblog.model.Post;
import jk.springblog.model.User;
import jk.springblog.model.enums.CategoryEnum;
import jk.springblog.repository.CommentRepository;
import jk.springblog.repository.PostsRepository;
import jk.springblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
        return postsRepository.findAll(Sort.by("id").descending());

    }

    public Post getPostById(Long post_id){
        if(postsRepository.findById(post_id).isPresent()) {
            return postsRepository.getOne(post_id);
        }
        return new Post();
    }

    public void addComment(Comment comment, Long post_id, Authentication auth){
        if(auth != null) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String loggedEmail = userDetails.getUsername();
            comment.setUser(userRepository.getByEmail(loggedEmail));
        }
        Post post = postsRepository.getOne(post_id);
        comment.setPost(post);
        commentRepository.save(comment);
    }

    public void savePost(Post post, String email){
        User user = userRepository.getByEmail(email);
        post.setUser(user);
        postsRepository.save(post);
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

    public boolean isAdmin(UserDetails userDetails){
        Set<GrantedAuthority> authorities = (Set<GrantedAuthority>)userDetails.getAuthorities();
        if (authorities.toString().contains("ROLE_ADMIN")){
            return true;
        }
        return false;
    }

    public Long getPostIdByCommentId(Long comment_id){
        Comment comment = commentRepository.getOne(comment_id);
        return comment.getPost().getId();
    }

    public void deleteComment (Long comment_id){
        commentRepository.deleteById(comment_id);
    }

    public void likePost(Long post_id){
        Post post = postsRepository.getOne(post_id);
        post.setLike_number(post.getLike_number()+1);
        postsRepository.save(post);
    }

    public void notLikePost(Long post_id){
        Post post = postsRepository.getOne(post_id);
        post.setNot_like_number(post.getNot_like_number()+1);
        postsRepository.save(post);
    }
    public List<Post>filterByCategory(CategoryEnum categoryEnum){
        return postsRepository.findAllByCategory(categoryEnum);
    }
}

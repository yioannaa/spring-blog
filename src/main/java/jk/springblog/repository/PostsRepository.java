package jk.springblog.repository;

import jk.springblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {

    Post findFirstById (Long post_id);
}

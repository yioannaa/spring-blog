package jk.springblog.repository;

import jk.springblog.model.Post;
import jk.springblog.model.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {
    List<Post>findAllByCategory(CategoryEnum category);
}

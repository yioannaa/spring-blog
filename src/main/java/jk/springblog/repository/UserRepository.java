package jk.springblog.repository;

import jk.springblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail (String email);
    User findFirstById(Long user_id);


}

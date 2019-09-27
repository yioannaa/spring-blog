package jk.springblog.service;


import jk.springblog.model.Role;
import jk.springblog.model.User;
import jk.springblog.repository.PostsRepository;
import jk.springblog.repository.RoleRepository;
import jk.springblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean passwordCheck(String password, String password_confirm){
        if(password.equals(password_confirm)){
            return true;
        }
        return false;
    }

    public void registerUser(User user){
        user.addRole(roleRepository.getOne(1L));
        //szyfrowanie hasła
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Role getAdminRole(){
        return roleRepository.getOne(2L);
    }

    public User getUserById (Long user_id){
        return userRepository.getOne(user_id);
    }

    public void addAdminRole (Long user_id){
        User user = getUserById(user_id);
        user.addRole(getAdminRole());
        userRepository.save(user);
    }

    public void subAdminRole(Long user_id){
        User user = getUserById(user_id);
        user.subRole(getAdminRole());
        userRepository.save(user);
    }
}

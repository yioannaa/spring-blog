package jk.springblog.service;

import jk.springblog.model.User;
import jk.springblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class MyUserDetailService implements UserDetailsService {

    UserRepository userRepository;


    @Autowired
    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);
        org.springframework.security.core.userdetails.User.UserBuilder userBuilder = null;
        if (user != null){
            String username;
            userBuilder = org.springframework.security.core.userdetails.User.withUsername(s);
            userBuilder.disabled(false).password(user.getPassword())
                    .roles(user.getRoles().stream().map(role -> role.getRole()).toArray(String[]::new))
                    .authorities(user.getRoles().stream().map(role -> role.getRole()).toArray(String[]::new));
        }else{
            throw new UsernameNotFoundException("User not found");
        }
        return userBuilder.build();
    }
}

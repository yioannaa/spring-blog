package jk.springblog.configuration;

import jk.springblog.handlers.MyAuthorityLoginSuccessHandler;
import jk.springblog.handlers.MyLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final
    UserDetailsService userDetailsService;

    private final MyAuthorityLoginSuccessHandler myAuthorityLoginSuccessHandler;

    private final MyLogoutSuccessHandler myLogoutSuccessHandler;


    @Autowired
    public SpringSecurityConfiguration(UserDetailsService userDetailsService,
                                       MyAuthorityLoginSuccessHandler myAuthorityLoginSuccessHandler,
                                       MyLogoutSuccessHandler myLogoutSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.myAuthorityLoginSuccessHandler = myAuthorityLoginSuccessHandler;
        this.myLogoutSuccessHandler = myLogoutSuccessHandler;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/addpost*").hasAnyAuthority("ADMIN","MOD")
                .antMatchers("/**").permitAll()
                .and().formLogin()
//                .loginPage("/login")
//                .successForwardUrl("/")
                .successHandler(myAuthorityLoginSuccessHandler)
                .and().logout()
                .logoutSuccessHandler(myLogoutSuccessHandler)
//                .logoutSuccessUrl("/")
                .permitAll()
                .and().csrf().disable();
    }
    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }


}

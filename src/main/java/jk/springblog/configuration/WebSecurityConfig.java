package jk.springblog.configuration;
;
import jk.springblog.filter.SocialMediaLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@EnableOAuth2Client
@Component
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers("/addpost").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/update/*").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/delete/*").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/admin").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().permitAll()
                .and()
                .csrf().disable()
                .formLogin().loginPage("/login")
                .usernameParameter("login")
                .passwordParameter("password")
                .loginProcessingUrl("/login-process")
                .failureUrl("/errorLogin")
                .defaultSuccessUrl("/")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/")
                .and()
                .addFilterBefore(socialMediaLoginFilter.authFilter(), BasicAuthenticationFilter.class);


    }

    @Autowired
    DataSource dataSource;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SocialMediaLoginFilter socialMediaLoginFilter;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery("SELECT u.email, u.password, u.activity FROM user u WHERE u.email = ?")
                .authoritiesByUsernameQuery("SELECT u.email, r.role_name FROM " +
                        "user u  JOIN user_role ur ON (u.id = ur.user_id) " +
                        "JOIN role r ON (r.id = ur.role_id) " +
                        "WHERE u.email = ?")
                .dataSource(dataSource).passwordEncoder(passwordEncoder);


    }

}

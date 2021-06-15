package com.hss.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class DemoApplicationConfig {

    /**
     * 添加Security的用户
     * @return
     */
    @Bean
    public UserDetailsService myUserDetailsService(){
//        把用户存储在内存中
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        构造用户的信息
        String [] [] usersGroupAndRoles = {
                {"jack","password","ROLE_ACTIVITI_USER","GROUP_activitiTeam"},
                {"rose","password","ROLE_ACTIVITI_USER","GROUP_activitiTeam"},
                {"tom","password","ROLE_ACTIVITI_USER","GROUP_activitiTeam"},
                {"jerry","password","ROLE_ACTIVITI_USER","GROUP_activitiTeam"},
                {"other","password","ROLE_ACTIVITI_USER","GROUP_activitiTeam"},
                {"system","password","ROLE_ACTIVITI_USER","GROUP_otherTeam"},
                {"admin","password","ROLE_ACTIVITI_ADMIN"}
        };

        for (String[] users : usersGroupAndRoles) {
            List<String> authStrList = Arrays.asList(Arrays.copyOfRange(users, 2, users.length));
            log.info("new user info:{}",authStrList);

            inMemoryUserDetailsManager.createUser(new User(users[0]
                    ,passwordEncoder().encode(users[1])
                    ,authStrList.stream().map(str -> new SimpleGrantedAuthority(str)).collect(Collectors.toList())));
        }

        return inMemoryUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

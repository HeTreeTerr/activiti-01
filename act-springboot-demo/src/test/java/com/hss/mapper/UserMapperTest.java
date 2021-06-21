package com.hss.mapper;

import com.hss.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectUserList() {
        List<User> users = userMapper.selectUserList();
        for(User user : users){
            log.info("--------------------");
            log.info("user:id={}",user.getId());
            log.info("user:name={}",user.getUsername());
        }
    }

    @Test
    public void selectOneUser() {
    }

    @Test
    public void selectOneUserByName() {
    }
}
package com.hss.controller;

import com.hss.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoginControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SecurityUtil securityUtil;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getUserName() throws Exception {
//        1.模拟用户登录
        securityUtil.logInAs("jack");

//        2.发起请求
        String responseString = mockMvc.perform(MockMvcRequestBuilders.get("/login/name")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .param("username","jack")
//                .param("password","123456")
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

//        3.在Controller 中加 @ResponseBody 可输出要返回的内容
        log.info("res={}", responseString);
    }
}
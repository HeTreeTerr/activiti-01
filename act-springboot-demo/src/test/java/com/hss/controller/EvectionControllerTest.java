package com.hss.controller;

import com.hss.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EvectionControllerTest {

    private MockMvc mockMvc;

    // 1.定义一个变量保存session
    private MockHttpSession session;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SecurityUtil securityUtil;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //2.初始化
        session = new MockHttpSession();
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findAll1() {
    }

    @Test
    public void addEvection() throws Exception {
//        1.模拟用户登录
        securityUtil.logInAs("jack");
        session.setAttribute("userid",1L);

        String evectionJson = "{\n" +
                "\"evectionName\":\"jack出差申请单1\",\n" +
                "\"num\":5,\n" +
                "\"destination\":\"出差\",\n" +
                "\"reson\":\"签约\"\n" +
                "}";

//        2.发起请求
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/evection/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(evectionJson)
                .session(session)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

//        3.在Controller 中加 @ResponseBody 可输出要返回的内容
        log.info("res={}", responseString);
    }
}
package com.hss.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ActFlowCommServiceTest {

    @Autowired
    private ActFlowCommService actFlowCommService;

    @Test
    public void saveNewDeploy() {
    }

    @Test
    public void deleteDeploy() {
        actFlowCommService.deleteDeploy("evection",true);
        log.info("删除成功！");
    }
}
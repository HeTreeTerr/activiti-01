package com.hss.mapper;

import com.hss.entity.SiteMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SiteMessageMapperTest {

    @Autowired
    private SiteMessageMapper siteMessageMapper;

    @Test
    public void selectMsgList() {
        List<SiteMessage> siteMessages = siteMessageMapper.selectMsgList(1L);
        for(SiteMessage siteMessage : siteMessages){
            log.info("-------------------");
            log.info("siteMessage:id=",siteMessage.getId());
            log.info("siteMessage:content=",siteMessage.getContent());
        }
    }

    @Test
    public void selectOneMsg() {
    }

    @Test
    public void updateMsgRead() {
    }

    @Test
    public void insertMsg() {
    }
}
package com.hss.mapper;

import com.hss.entity.FlowInfo;
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
public class FlowMapperTest {

    @Autowired
    private FlowMapper flowMapper;

    @Test
    public void selectFlowList() {
        List<FlowInfo> flowInfos = flowMapper.selectFlowList();
        for(FlowInfo flowInfo : flowInfos){
            log.info("-----------------");
            log.info("flowInfo:id={}",flowInfo.getId());
            log.info("flowInfo:flowname={}",flowInfo.getFlowname());
        }
    }

    @Test
    public void updateFlowDeployState() {
    }

    @Test
    public void selectOneFlow() {
    }
}
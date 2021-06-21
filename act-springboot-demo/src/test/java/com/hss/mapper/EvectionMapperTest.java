package com.hss.mapper;

import com.hss.entity.Evection;
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
public class EvectionMapperTest {

    @Autowired
    private EvectionMapper evectionMapper;

    @Test
    public void selectAll() {
        List<Evection> evections = evectionMapper.selectAll(1L);
        for(Evection evection : evections){
            log.info("---------------------");
            log.info("evection:id={}",evection.getId());
            log.info("evection:evectionName={}",evection.getEvectionName());
        }
    }

    @Test
    public void startTask() {
    }

    @Test
    public void endTask() {
    }

    @Test
    public void selectOne() {
    }

    @Test
    public void save() {
    }
}
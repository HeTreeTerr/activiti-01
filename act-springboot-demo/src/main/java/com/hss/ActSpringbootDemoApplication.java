package com.hss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hss.mapper")
public class ActSpringbootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActSpringbootDemoApplication.class, args);
    }

}

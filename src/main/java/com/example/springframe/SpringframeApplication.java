package com.example.springframe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.springframe.mapper")
public class SpringframeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringframeApplication.class, args);
    }

}

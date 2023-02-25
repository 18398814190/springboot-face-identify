package com.wjp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  springboot启动类
 */
@SpringBootApplication
@MapperScan("com.wjp.mapper")
public class Start {

    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
    }

}

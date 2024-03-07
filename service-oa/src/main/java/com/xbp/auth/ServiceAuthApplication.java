package com.xbp.auth;

import javafx.application.Application;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 14343
 * @version 1.0
 * @description: TODO
 * @date 2024/3/1 9:29
 */
@SpringBootApplication
@ComponentScan("com.xbp")
public class ServiceAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthApplication.class,args);
    }
}

package com.study.service.user;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
//@ComponentScan(basePackages = {"com.study"}) --不能少,少了swagger识别不到接口
//@MapperScan 是加载xml的路径
@SpringBootApplication
@MapperScan("com.study.service.user.mapper")
@ComponentScan(basePackages = {"com.study"})
@EnableDiscoveryClient
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}

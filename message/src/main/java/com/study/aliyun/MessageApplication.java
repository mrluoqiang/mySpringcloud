package com.study.aliyun;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = {"com.study"}) --不能少,少了swagger识别不到接口
//@MapperScan 是加载xml的路径
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源自动配置
//@MapperScan("com.study.service.user.mapper")
@EnableDiscoveryClient
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }
}

package com.study.aliyun.config.oss;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Data
@Configuration
public class AliYunOssParam implements InitializingBean {

    public static String ossEndpoint = null;
    public static String ossAccessKeyId = null;
    public static String ossAccessKeySecret = null;
    public static String ossBucketName = null;
    public static String ossUrlPrex = null;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.urlPrex}")
    private String urlPrex;

    @Override
    public void afterPropertiesSet() throws Exception {
        ossEndpoint =endpoint;
        ossAccessKeyId = accessKeyId;
        ossAccessKeySecret = accessKeySecret;
        ossBucketName = bucketName;
        ossUrlPrex = urlPrex;
    }
}

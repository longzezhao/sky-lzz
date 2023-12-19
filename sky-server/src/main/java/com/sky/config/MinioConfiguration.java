package com.sky.config;

import com.sky.utils.MinioProp;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {

    @Autowired
    private MinioProp minioProp;

    @Bean
    public MinioClient minioClient(){
        MinioClient client = new MinioClient(minioProp.getEndpoint(), minioProp.getAccesskey(),minioProp.getSecretkey());
        return client;
    }

}

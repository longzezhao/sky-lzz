//package com.sky.config;
//
//import com.sky.properties.MinioProperties;
//import io.minio.MinioClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
////@Configuration
//public class MinioConfiguration {
//
//    @Autowired
//    private MinioProperties minioProp;
//
//    @Bean
//    public MinioClient minioClient(){
//        MinioClient client = new MinioClient(minioProp.getEndpoint(), minioProp.getAccesskey(),minioProp.getSecretkey());
//        return client;
//    }
//
//}

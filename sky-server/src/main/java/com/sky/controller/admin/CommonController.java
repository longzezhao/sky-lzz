package com.sky.controller.admin;

import com.sky.result.Result;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


/*
* 通用接口
* */
@RestController
@RequestMapping("/admin/common")
@Api("通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private MinioClient minioClient;

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result upload(MultipartFile file) {
        log.info("文件上传:{}",file);
        String originalFilename = file.getOriginalFilename();
        String url;
        try{

            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket("sky-lzz-bucket")
                    .object(originalFilename)
                    .stream(file.getInputStream(), file.getSize(), -1L)
                    .contentType(file.getContentType())
                    .build();

            minioClient.putObject(args);

            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket("sky-lzz-bucket")
                            .object(originalFilename)
                            .expiry(60*60)
                            .build()
            );

        }catch (Exception e){
            log.error("上传失败",e);
            return Result.success("上传失败");
        }

        return Result.success(url);
    }

    @GetMapping("/file")
    public Result getMinios(@PathParam("filename") String filename) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, InvalidExpiresRangeException, ServerException, InternalException, NoSuchAlgorithmException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        log.info("filename:{}",filename);
        int expires = 60 * 60; // 1 hour in seconds

        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket("sky-lzz-bucket")
                        .object(filename)
                        .expiry(expires)
                        .build()
        );

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("view",url);

        return Result.success(hashMap);

    }


}

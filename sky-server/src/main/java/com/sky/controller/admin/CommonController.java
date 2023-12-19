package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.MinioProp;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;


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
        try{

            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket("sky-bucket")
                    .object(originalFilename)
                    .stream(file.getInputStream(), file.getSize(), -1L)
                    .contentType(file.getContentType())
                    .build();

            minioClient.putObject(args);

        }catch (Exception e){
            log.error("上传失败",e);
            return Result.success("上传失败");
        }


        return Result.success("上传成功");
    }

    @GetMapping("/file")
    public Result getMinios(@PathParam("filename") String filename) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, InvalidExpiresRangeException, ServerException, InternalException, NoSuchAlgorithmException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        log.info("filename:{}",filename);
        int expires = 60 * 60; // 1 hour in seconds

        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket("sky-bucket")
                        .object(filename)
                        .expiry(expires)
                        .build()
        );

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("view",url);

        return Result.success(hashMap);

    }


}

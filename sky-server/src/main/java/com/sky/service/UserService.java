package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

public interface UserService {

    /*
    *  微信登录
    * */
    @PostMapping("/login")
    @ApiOperation("微信登录")
    User wxLogin(UserLoginDTO userLoginDTO);

}

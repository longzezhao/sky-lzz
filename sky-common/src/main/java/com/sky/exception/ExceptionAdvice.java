package com.sky.exception;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public Result exceptionHandler(Exception e){
        log.error("执行异常",e);
        return Result.error(e.getMessage());
    }

}

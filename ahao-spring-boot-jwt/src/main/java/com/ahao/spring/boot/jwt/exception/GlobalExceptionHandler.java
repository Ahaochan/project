package com.ahao.spring.boot.jwt.exception;

import com.ahao.domain.entity.AjaxDTO;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice("com.ahao")
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 拦截token异常
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public AjaxDTO wxErrorException(JwtException e) {
        logger.error("token异常:", e);
        return AjaxDTO.failure(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public AjaxDTO runtimeException(RuntimeException e) {
        logger.error("运行时异常:", e);
        return AjaxDTO.failure("服务器异常! 请稍候重试!");
    }
}

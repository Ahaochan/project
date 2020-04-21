package com.ahao.spring.boot.mybatis.plus.exception;

import com.ahao.domain.entity.AjaxDTO;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.PersistenceException;

@ControllerAdvice("com.ahao")
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PersistenceException.class)
    public AjaxDTO persistenceException(PersistenceException ex) {
        logger.error("Mybatis 错误", ex);
        Throwable cause = ex.getCause();
        String errorMsg = cause.getMessage();
        return AjaxDTO.failure(errorMsg, errorMsg);
    }
    @ExceptionHandler(MyBatisSystemException.class)
    public AjaxDTO myBatisSystemException(MyBatisSystemException ex) {
        logger.error("Mybatis 错误", ex);
        Throwable cause = ex.getCause(); // 获取 MyBatisSystemException 的 cause
        if(cause != null && cause.getCause() != null) {
            cause = cause.getCause(); // 获取 PersistenceException 的 cause
        }
        String errorMsg = cause == null ? ex.getMessage() : cause.getMessage();
        return AjaxDTO.failure(errorMsg, errorMsg);
    }
}

package com.ahao.spring.exception;

import com.ahao.domain.entity.AjaxDTO;
import com.ahao.web.exception.AhaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice("com.ahao")
@Order(0)
public class BizExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(BizExceptionHandler.class);

    /**
     * 拦截业务异常
     */
    @ExceptionHandler(AhaoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public AjaxDTO ahaoException(AhaoException e) {
        logger.error("业务异常:", e);
        return AjaxDTO.failure(e.getMessage());
    }
}

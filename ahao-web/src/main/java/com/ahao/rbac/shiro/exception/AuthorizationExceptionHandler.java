package com.ahao.rbac.shiro.exception;

import com.ahao.util.spring.AppUtils;
import org.apache.shiro.authc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice("com.ahao")
public class AuthorizationExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationExceptionHandler.class);

    @ExceptionHandler(value = DisabledAccountException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String disabledAccountException(DisabledAccountException ex) {
        if(AppUtils.isDev()) {
            logger.debug("禁用的帐号", ex);
        }
        logger.debug("禁用的帐号, {}", ex.getMessage());
        return "禁用的帐号";
    }

    @ExceptionHandler(value = LockedAccountException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String lockedAccountException(LockedAccountException ex) {
        if(AppUtils.isDev()) {
            logger.debug("锁定的帐号", ex);
        }
        logger.debug("锁定的帐号, {}", ex.getMessage());
        return "锁定的帐号";
    }

    @ExceptionHandler(value = UnknownAccountException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String unknownAccountException(UnknownAccountException ex) {
        if(AppUtils.isDev()) {
            logger.debug("未知的帐号", ex);
        }
        logger.debug("未知的帐号, {}", ex.getMessage());
        return "未知的帐号";
    }

    @ExceptionHandler(value = ExcessiveAttemptsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String excessiveAttemptsException(ExcessiveAttemptsException ex) {
        if(AppUtils.isDev()) {
            logger.debug("登录失败次数过多", ex);
        }
        logger.debug("登录失败次数过多, {}", ex.getMessage());
        return "登录失败次数过多";
    }

    @ExceptionHandler(value = IncorrectCredentialsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String incorrectCredentialsException(IncorrectCredentialsException ex) {
        if(AppUtils.isDev()) {
            logger.debug("错误的凭证", ex);
        }
        logger.debug("错误的凭证, {}", ex.getMessage());
        return "错误的凭证";
    }

    @ExceptionHandler(value = ExpiredCredentialsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String expiredCredentialsException(ExpiredCredentialsException ex) {
        if(AppUtils.isDev()) {
            logger.debug("过期的凭证", ex);
        }
        logger.debug("过期的凭证, {}", ex.getMessage());
        return "过期的凭证";
    }

    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String authenticationException(AuthenticationException ex) {
        logger.error("未知的权限异常, {}", ex);
        return "未知的权限异常";
    }
}

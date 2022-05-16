package moe.ahao.spring.boot.jwt.exception;

import io.jsonwebtoken.JwtException;
import moe.ahao.domain.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice("moe.ahao")
@Order(0)
public class JwtExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(JwtExceptionHandler.class);

    /**
     * 拦截token异常
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result<Object> jwtException(JwtException e) {
        logger.error("token异常:", e);
        return Result.failure(e.getMessage());
    }
}

package moe.ahao.spring.boot.exception;

import me.chanjar.weixin.common.error.WxErrorException;
import moe.ahao.domain.entity.AjaxDTO;
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
public class WxExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(WxExceptionHandler.class);

    /**
     * 拦截业务异常
     */
    @ExceptionHandler(WxErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public AjaxDTO wxErrorException(WxErrorException e) {
        logger.error("微信接口异常:", e);
        return AjaxDTO.failure(e.getError());
    }
}

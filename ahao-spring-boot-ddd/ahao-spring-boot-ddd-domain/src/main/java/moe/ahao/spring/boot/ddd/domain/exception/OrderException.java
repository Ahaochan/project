package moe.ahao.spring.boot.ddd.domain.exception;

import moe.ahao.exception.BizException;

/* package */ class OrderException extends BizException {
    public OrderException(int code, String message) {
        super(code, message);
    }
}

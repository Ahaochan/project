package moe.ahao.maven.archetype.domain.exception;

import moe.ahao.exception.BizException;

/* package */ class OrderException extends BizException {
    public OrderException(int code, String message) {
        super(code, message);
    }
}

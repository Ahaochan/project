package moe.ahao.tend.consistency.core.infrastructure.exceptions;

import moe.ahao.exception.BizException;

/**
 * 异常类
 **/
public class ConsistencyException extends BizException {
    public ConsistencyException(int code, String message) {
        super(code, message);
    }

    public ConsistencyException(String message) {
        super(-1, message);
    }

    public ConsistencyException(Throwable e) {
        super(-1, e.getMessage(), e);
    }
}

package com.ruyuan.consistency.exceptions;

/**
 * 异常类
 *
 * @author zhonghuashishan
 **/
public class ConsistencyException extends RuntimeException {

    public ConsistencyException() {
    }

    public ConsistencyException(Exception e) {
        super(e);
    }

    public ConsistencyException(String message) {
        super(message);
    }


}

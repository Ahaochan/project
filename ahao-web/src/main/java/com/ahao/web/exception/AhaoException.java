package com.ahao.web.exception;


import org.springframework.http.HttpStatus;

public class AhaoException extends RuntimeException {

    private Integer code;

    private String message;

    public static AhaoException create(HttpStatus status, String message) {
        AhaoException exception = new AhaoException(message);
        exception.code = status.value();
        exception.message = message;
        return exception;
    }

    private AhaoException(String message) {
        super(message);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

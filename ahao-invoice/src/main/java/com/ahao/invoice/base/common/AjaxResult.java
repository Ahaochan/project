package com.ahao.invoice.base.common;

import java.io.Serializable;

/**
 * Created by Avalon on 2017/5/10.
 */
public class AjaxResult<T> implements Serializable {
    public static final Integer AJAX_STATUS_CODE_SUCCESS = 0;
    public static final Integer AJAX_STATUS_CODE_WARN    = 1;
    public static final Integer AJAX_STATUS_CODE_ERROR   = 2;

    private Integer code;
    private String msg;
    private T result;

    public AjaxResult(){
        this(AJAX_STATUS_CODE_SUCCESS);
    }

    public AjaxResult(Integer code) {
        this(code, "");
    }

    public AjaxResult(Integer code, String msg) {
        this(code, msg, null);
    }

    public AjaxResult(Integer code, String msg, T result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public static AjaxResult success(){
        return new AjaxResult(AJAX_STATUS_CODE_SUCCESS, "操作成功");
    }

    public static AjaxResult warn(){
        return new AjaxResult(AJAX_STATUS_CODE_WARN);
    }

    public static AjaxResult error(){
        return new AjaxResult(AJAX_STATUS_CODE_ERROR);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AjaxResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }
}

package com.ahao.commons.entity;

/**
 * Created by Ahaochan on 2017/7/18.
 * 传递Ajax的Data Transfer Object
 */
public class AjaxDTO {
    private static final int FAILURE = 0;
    private static final int SUCCESS = 1;

    public static AjaxDTO failure(String msg) {
        return new AjaxDTO(FAILURE, msg, null);
    }

    public static AjaxDTO failure(Object obj) {
        return new AjaxDTO(FAILURE, "", obj);
    }

    public static AjaxDTO success(String msg) {
        return new AjaxDTO(SUCCESS, msg, null);
    }

    public static AjaxDTO success(Object obj) {
        return new AjaxDTO(SUCCESS, "", obj);
    }

    public static AjaxDTO get(boolean success) {
        return new AjaxDTO(success ? SUCCESS : FAILURE, "", null);
    }


    private int result;
    private String msg;
    private Object obj;

    private AjaxDTO(int result, String msg, Object obj) {
        this.result = result;
        this.msg = msg;
        this.obj = obj;
    }

    public AjaxDTO obj(Object obj) {
        this.obj = obj;
        return this;
    }

    public AjaxDTO result(int result) {
        this.result = result;
        return this;
    }

    public AjaxDTO msg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public Object getObj() {
        return obj;
    }
}

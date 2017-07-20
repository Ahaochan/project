package com.ahao.entity;

/**
 * Created by Ahaochan on 2017/7/18.
 */
public class AjaxDTO {
    public static final int FAILURE = 0;
    public static final int SUCCESS = 1;

    public static AjaxDTO getFailure(String msg){
        return new AjaxDTO(FAILURE, msg, null);
    }

    public static AjaxDTO getSuccess(String msg){
        return new AjaxDTO(SUCCESS, msg, null);
    }

    public static AjaxDTO get(int result){
        return new AjaxDTO(result, "", null);
    }

    public static AjaxDTO get(int result, String msg){
        return new AjaxDTO(result, msg, null);
    }

    public static AjaxDTO get(int result, String msg, Object obj){
        return new AjaxDTO(result, msg, obj);
    }

    /**
     * 0表示失败 1表示成功
     */
    private int result;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 附加对象，用来存储一些特定的返回信息
     */
    private Object obj;

    public AjaxDTO(int result, String msg, Object obj) {
        this.result = result;
        this.msg = msg;
        this.obj = obj;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}

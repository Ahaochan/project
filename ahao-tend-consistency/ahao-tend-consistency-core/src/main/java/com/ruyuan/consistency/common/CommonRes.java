package com.ruyuan.consistency.common;

import java.io.Serializable;

/**
 * 返回值结果
 * @author zhonghuashishan
 */
public class CommonRes<T> implements Serializable {

    private Boolean success;

    private T data;

    public CommonRes() {
    }

    public CommonRes(Boolean success) {
        this.success = success;
    }

    public CommonRes(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public static <T> CommonRes<T> success(T data) {
        return new CommonRes<T>(true, data);
    }

    public static <CommonError> CommonRes<CommonError> fail(CommonError data) {
        return new CommonRes<>(false, data);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
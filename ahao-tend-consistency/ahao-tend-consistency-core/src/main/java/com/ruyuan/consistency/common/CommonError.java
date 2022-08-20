package com.ruyuan.consistency.common;

import java.io.Serializable;

/**
 * 异常信息类
 * @author zhonghuashishan
 */
public class CommonError implements Serializable {

    /**
     * 错误码
     */
    private Integer errCode;
    /**
     * 错误描述
     */
    private String errMsg;

    public CommonError() {
    }

    public CommonError(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public CommonError(EmBusinessError emBusinessError) {
        this.errCode = emBusinessError.getErrCode();
        this.errMsg  = emBusinessError.getErrMsg();
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
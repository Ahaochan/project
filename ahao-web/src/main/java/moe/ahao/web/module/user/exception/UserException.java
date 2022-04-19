package moe.ahao.web.module.user.exception;


import moe.ahao.exception.BizException;

/* package */ class UserException extends BizException {
    public UserException(int code, String message) {
        super(code, message);
    }
}

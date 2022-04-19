package moe.ahao.web.module.user.exception;


import moe.ahao.exception.BizExceptionEnum;

public enum UserExceptionEnum implements BizExceptionEnum<UserException> {
    USER_ID_IS_NULL(105001, "用户ID不能为空"),
    ;

    private int code;
    private String message;
    UserExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public UserException msg(Object... args) {
        return new UserException(code, String.format(message, args));
    }
}

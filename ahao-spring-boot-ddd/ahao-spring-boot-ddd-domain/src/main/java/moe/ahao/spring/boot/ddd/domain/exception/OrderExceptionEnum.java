package moe.ahao.spring.boot.ddd.domain.exception;

public enum OrderExceptionEnum {
    ORDER_NO_DUPLICATED(500001, "订单号%s重复"),
    ORDER_NOT_FOUND(500002, "订单号%s不存在"),
    ;

    private int code;
    private String message;
    OrderExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public OrderException msg(String... args) {
        return new OrderException(code, String.format(message, (Object[]) args));
    }
}

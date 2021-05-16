package moe.ahao.spring.boot.security.plugin.otp;

public class SendException extends RuntimeException {
	public SendException(String msg) {
		super(msg);
	}

	public SendException(String msg, Throwable e) {
		super(msg, e);
	}
}

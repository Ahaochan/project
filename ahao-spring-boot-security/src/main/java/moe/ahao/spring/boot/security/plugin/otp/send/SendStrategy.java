package moe.ahao.spring.boot.security.plugin.otp.send;

public interface SendStrategy {
	public void send(String token, String destination);
}

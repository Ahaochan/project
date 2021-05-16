//package moe.ahao.spring.boot.security.plugin.otp.send;
//
//import java.util.List;
//
//public class SmsSendStrategy implements SendStrategy {
//
//	private EmailSendStrategy emailSender;
//	private List<String> carrierDomains;
//
//	public SmsSendStrategy(EmailSendStrategy emailSender, List<String> carrierDomains) {
//		this.emailSender = emailSender;
//		this.carrierDomains = carrierDomains;
//	}
//
//	@Override
//	public void send(String token, String phoneNumber) {
//		for (String domain : carrierDomains) {
//			emailSender.send(token, phoneNumber + "@" + domain);
//		}
//	}
//}

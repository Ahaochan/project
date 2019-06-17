package com.ahao.spring.boot.mail;

import com.ahao.spring.boot.Starter;
import com.ahao.util.commons.lang.ReflectHelper;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
class MailTest {

    @Autowired
    private JavaMailSender mailSender;

    private String username;
    @BeforeEach
    void init() {
        username = ReflectHelper.getValue(mailSender, "username");
        Assumptions.assumeTrue(StringUtils.isNotBlank(username), "需要配置实际的邮箱配置");
    }

    @Test
    @DisplayName("发送纯文本邮件")
    void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo("844394093@qq.com");
        message.setSubject("主题: 简单邮件");
        message.setText("测试邮件内容2");
        mailSender.send(message);
    }

    @Test
    @DisplayName("发送有附件的邮件")
    void sendAttachmentsMail() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(username);
        helper.setTo("收件人@qq.com");
        helper.setSubject("主题: 有附件的邮件");
        helper.setText("有附件的邮件内容");

//        Resource file = new FileSystemResource(new File("test.jpg"));
        Resource file = new ByteArrayResource("hello world".getBytes(StandardCharsets.UTF_8));
        helper.addAttachment("附件-1.txt", file);
        helper.addAttachment("附件-2.txt", file);

        mailSender.send(mimeMessage);
    }

    @Test
    @DisplayName("发送Html的邮件")
    void sendHtmlMail() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(username);
        helper.setTo("收件人@qq.com");
        helper.setSubject("主题: Html");
        helper.setText("<html><body><h1>Hello world</h1></body></html>", true);

        mailSender.send(mimeMessage);
    }
}

package com.ahao.spring.boot.mail;

import com.ahao.spring.boot.Starter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = Starter.class)
public class MailTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("发件人@qq.com");
        message.setTo("收件人@qq.com");
        message.setSubject("主题: 简单邮件");
        message.setText("测试邮件内容");

        mailSender.send(message);
    }

    @Test
    public void sendAttachmentsMail() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("发件人@qq.com");
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
    public void sendHtmlMail() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("发件人@qq.com");
        helper.setTo("收件人@qq.com");
        helper.setSubject("主题: Html");
        helper.setText("<html><body><h1>Hello world</h1></body></html>", true);

        mailSender.send(mimeMessage);
    }
}

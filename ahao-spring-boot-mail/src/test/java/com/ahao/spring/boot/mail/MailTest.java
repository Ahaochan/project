package com.ahao.spring.boot.mail;

import com.ahao.spring.boot.Starter;
import com.ahao.util.spring.mail.MailHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
class MailTest {

    @Autowired
    private JavaMailSender mailSender;

    @BeforeEach
    void init() {
        String username = MailHelper.getProperties().getUsername();
        Assumptions.assumeTrue(StringUtils.isNotBlank(username), "需要配置实际的邮箱配置");
        Assertions.assertNotNull(mailSender);
    }

    @Test
    @DisplayName("发送纯文本邮件")
    void sendSimpleMail() {
        boolean success = MailHelper.sendText("844394093@qq.com", "主题: 简单邮件", "测试邮件内容");
        Assertions.assertTrue(success);
    }

    @Test
    @DisplayName("发送有附件的邮件")
    void sendAttachmentsMail() throws Exception {
        Resource file = new ByteArrayResource("hello world".getBytes(StandardCharsets.UTF_8));
        Map<String, InputStreamSource> files = new HashMap<>();
        files.put("附件-1.txt", file);
        files.put("附件-2.txt", file);

        boolean success = MailHelper.sendTextWithFile("844394093@qq.com", "主题: 简单邮件", "测试邮件内容", files);
        Assertions.assertTrue(success);
    }

    @Test
    @DisplayName("发送Html的邮件")
    void sendHtmlMail() throws Exception {
        boolean success = MailHelper.sendHtml("844394093@qq.com", "主题: Html", "<html><body><h1>Hello world</h1></body></html>");
        Assertions.assertTrue(success);
    }
}

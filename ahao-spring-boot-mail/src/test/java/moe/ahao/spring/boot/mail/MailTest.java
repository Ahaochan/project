package moe.ahao.spring.boot.mail;

import moe.ahao.util.spring.SpringContextHolder;
import moe.ahao.util.spring.mail.MailHelper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailSenderValidatorAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {SpringContextHolder.class,
    MailSenderAutoConfiguration.class, MailSenderValidatorAutoConfiguration.class})
public class MailTest {

    @Autowired
    private JavaMailSender mailSender;

    @BeforeEach
    public void init() {
        String username = MailHelper.getProperties().getUsername();
        Assumptions.assumeTrue(StringUtils.isNotBlank(username), "需要配置实际的邮箱配置");
        Assertions.assertNotNull(mailSender);
    }

    @Test
    @DisplayName("发送纯文本邮件")
    public void sendSimpleMail() throws Exception {
        MailHelper.setSubject("主题: 简单邮件")
            .setContent("测试邮件内容")
            .addToEmails(Arrays.asList("844394093@qq.com"))
            .addBccEmails(Arrays.asList("844394093@qq.com"))
            .send();
    }

    @Test
    @DisplayName("发送有附件的邮件")
    public void sendAttachmentsMail() throws Exception {
        Resource file = new ByteArrayResource("hello world".getBytes(StandardCharsets.UTF_8));
        Map<String, Resource> files = new HashMap<>();
        files.put("附件-1.txt", file);
        files.put("附件-2.txt", file);

        MailHelper.setSubject("主题: 简单邮件")
            .setContent("测试邮件内容")
            .addToEmails(Arrays.asList("844394093@qq.com"))
            .addBccEmails(Arrays.asList("844394093@qq.com"))
            .setExtendFile(files)
            .send();
    }

    @Test
    @DisplayName("发送Html的邮件")
    public void sendHtmlMail() throws Exception {
        MailHelper.setSubject("主题: Html")
            .setContent("<html><body><h1>Hello world</h1></body></html>", true)
            .addToEmails(Arrays.asList("844394093@qq.com"))
            .addBccEmails(Arrays.asList("844394093@qq.com"))
            .send();
    }
}

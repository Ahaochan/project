package moe.ahao.spring.boot.i18n;

import moe.ahao.spring.boot.Starter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
// @ContextConfiguration(classes = MessageSourceAutoConfiguration.class)
class SampleTest {
    @Autowired
    private MessageSource messageSource;

    @Test
    void simple() {
        // RequestContextUtils.getLocale(request);
        // LocaleContextHolder.getLocale();
        Assertions.assertEquals("消息1", messageSource.getMessage("message1", null, Locale.CHINA));
        Assertions.assertEquals("message1", messageSource.getMessage("message1", null, Locale.US));

        Assertions.assertEquals("消息2", messageSource.getMessage("message2", null, Locale.CHINA));
        Assertions.assertEquals("message2", messageSource.getMessage("message2", null, Locale.US));
    }
}

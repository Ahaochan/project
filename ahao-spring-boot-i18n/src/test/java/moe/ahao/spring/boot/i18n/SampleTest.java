package moe.ahao.spring.boot.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {MessageSourceAutoConfiguration.class})
public class SampleTest {

    @Autowired
    private MessageSource messageSource;

    @Test
    public void simple() {

        // RequestContextUtils.getLocale(request);
        // LocaleContextHolder.getLocale();

        Assertions.assertEquals("消息1", messageSource.getMessage("message1", null, Locale.CHINA));
        Assertions.assertEquals("message1", messageSource.getMessage("message1", null, Locale.US));

        Assertions.assertEquals("消息2", messageSource.getMessage("message2", null, Locale.CHINA));
        Assertions.assertEquals("message2", messageSource.getMessage("message2", null, Locale.US));
    }
}

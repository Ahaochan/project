package com.ahao.spring.boot.async;

import com.ahao.spring.boot.Starter;
import com.ahao.spring.boot.async.service.TestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Future;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = Starter.class)
public class AsyncTest {
    @Autowired
    private TestService testService;

    @Test
    public void testVoid() throws Exception {
        String msg = "hello World";
        testService.executeVoid(msg);
        Assertions.assertNull(TestService.value);

        Thread.sleep(2000);
        Assertions.assertEquals(msg, TestService.value);
    }

    @Test
    public void testFuture() throws Exception {
        int i = 1;
        Future<Integer> future = testService.executeFuture(i);
        Assertions.assertEquals(Integer.valueOf(i + 100), future.get());
    }

    @Test
    public void testException() throws Exception {
        // 1. 重定向输出
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream sout = System.out;
        System.setOut(new PrintStream(baos));

        testService.executeException();
        Thread.sleep(3000);
        String msg = baos.toString();
        System.setOut(sout);

        Assertions.assertEquals("com.ahao.spring.boot.async.service.TestService#executeException(), 错误信息: 错误\r\n", msg);

    }
}

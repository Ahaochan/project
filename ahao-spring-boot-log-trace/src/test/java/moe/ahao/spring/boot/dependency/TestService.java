package moe.ahao.spring.boot.dependency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    @Async
    public void async(CountDownLatch latch) {
        logger.trace("async trace");
        logger.debug("async debug");
        logger.info("async info");
        logger.warn("async warn");
        logger.error("async error");
        latch.countDown();
    }
}

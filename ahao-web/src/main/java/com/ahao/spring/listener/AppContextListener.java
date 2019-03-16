package com.ahao.spring.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.*;
import org.springframework.stereotype.Component;

@Component
public class AppContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @EventListener
//    @Async
    public void contextRefreshed(ContextRefreshedEvent event) {
        logger.debug("ContextRefreshedEvent 事件触发!");
    }

    @EventListener
    public void contextStarted(ContextStartedEvent event) {
        logger.debug("ContextStartedEvent 事件触发!");
    }

    @EventListener
    public void contextStopped(ContextStoppedEvent event) {
        logger.debug("ContextStoppedEvent 事件触发!");
    }

    @EventListener
    public void contextClosed(ContextClosedEvent event) {
        logger.debug("ContextClosedEvent 事件触发!");
    }
}

package moe.ahao.web.module.inventory.listener;

import moe.ahao.web.module.inventory.request.RequestProcessorThreadPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        RequestProcessorThreadPool threadPool = RequestProcessorThreadPool.getInstance();
        threadPool.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        RequestProcessorThreadPool threadPool = RequestProcessorThreadPool.getInstance();
        threadPool.destroy();
    }
}

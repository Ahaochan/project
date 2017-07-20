package com.ahao.listener;

import com.ahao.config.SpringConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class SpringConfigInitListener extends ContextLoaderListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(SpringConfigInitListener.class);

	public void contextInitialized(ServletContextEvent event) {
		logger.info("********Spring配置文件开始装载SpringConfigInitListener********");

		// 初始化Spring：call Spring's context ContextLoaderListener to initialize
		// all the context files specified in web.xml
		super.contextInitialized(event);

		// 取得ApplicationContext
		ApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(event.getServletContext());
		if (context != null) {
			SpringConfig.setApplicationContext(context);
			SpringConfig.setInitialized(true);
			logger.info("User ContextLoaderListener load Spring config succeed.");
		} else {
			logger.error("User ContextLoaderListener load Spring config error.");
		}
	}
}

package com.ahao.commons.servlet;

import com.ahao.commons.config.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * 系统Servlet的基础类<br>
 * 主要是初始化，及载入系统资源。
 *
 * @author Ahaochan
 */

public class ConfigServlet extends HttpServlet {
    private static final long serialVersionUID = 4286666206517213090L;
    private static final Logger logger = LoggerFactory.getLogger(ConfigServlet.class);

    @SuppressWarnings("unused")
    private ServletConfig config;
    @SuppressWarnings("unused")
    private ServletContext context;

    /**
     * 初始化
     */
    public void init(ServletConfig config) throws ServletException {
        logger.info("********开始初始化系统环境********");
        super.init(config);
        this.config = config;
        this.context = config.getServletContext();
        initSystem(config);
    }

    /**
     * 初始化系统参数
     */
    private void initSystem(ServletConfig config) throws ServletException {
        // 载入系统配置信息
        SystemConfig.instance();
    }
}

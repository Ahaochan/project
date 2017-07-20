package com.ahao.servlet;

import com.ahao.config.SystemConfig;
import com.ahao.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HttpServletBean;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * 系统Servlet的基础类<br>
 * 主要是初始化，及载入系统资源。
 *
 * @author Ahaochan
 */

public class ConfigServlet extends HttpServletBean {
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
     *
     * @throws ServletException
     */
    private void initSystem(ServletConfig config) throws ServletException {
        try {
            // 取得系统的目录路径
            ApplicationContext.setSystemRoot(config.getServletContext().getRealPath("/"));
            // 载入系统配置信息
            initSystemConfig();
        } catch (Exception ex) {
            logger.error("初始化系统参数出错", ex);
            throw new ServletException(ex.getMessage());
        }
    }

    /**
     * 初始化系统配置
     */
    private void initSystemConfig() {
        SystemConfig.getInstance();
    }

    public void destroy() {

    }
}

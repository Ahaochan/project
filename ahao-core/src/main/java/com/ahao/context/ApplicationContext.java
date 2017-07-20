package com.ahao.context;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 应用程序上下文对象
 *
 * @author Ahaochan
 */
public final class ApplicationContext implements Context {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    private static String systemRoot = "";

    /**
     * 从类路径中获取系统的当前路径.
     */
    private static String initFromCodeSource() {
        String root = ApplicationContext.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (root.indexOf("WEB-INF/classes/") > 0) { // 类路径中
            root = root.substring(0, root.indexOf("WEB-INF/classes/"));
        } else { // 打为jar包
            root = root.substring(0, root.indexOf("WEB-INF/lib/"));
        }
        return root;
    }

    public static String getSystemRoot() {
        // 如果没被设置过（如在非Web容器下运行和测试）
        if (org.apache.commons.lang3.StringUtils.isEmpty(systemRoot)) {
            logger.error("未设置系统根目录，将从类路径中获取系统的当前路径.");
            systemRoot = initFromCodeSource();
        }

        return systemRoot;
    }

    public static void setSystemRoot(String root) {
        systemRoot = root;
        // 判断目录最后是否有/或\符号，如果没有，则加上
        String separator = File.separator;
        int pos = systemRoot.lastIndexOf(separator);
        if (pos != systemRoot.length() - 1)
            systemRoot = systemRoot + separator;

        systemRoot = StringUtils.replace(systemRoot, "\\", "/");
    }
}

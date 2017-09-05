package com.ahao.config;

import com.ahao.util.RequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Spring配置工具类，用以读取Spring Bean配置，属性文件配置，XML文件配置等信息.<br>
 * 主要用于在命令行启动时配置Spring，或者用于WEB应用中自行加载Spring的情况.
 */
public class SpringConfig {
    private static final Logger log = LoggerFactory.getLogger(SystemConfig.class);

    private static ApplicationContext context;

    /**
     * 初始化状态，true代表已初始化，否则没有初始化
     */
    private static boolean initialized;

    /**
     * 单例模式
     */
    private volatile static SpringConfig instance;

    private SpringConfig() {
        if (!initialized) {
            loadFromClassPath();
            setInitialized(true);
        }
    }

    /**
     * 从类路径中装载Spring配置.
     */
    private void loadFromClassPath() {
        try {
            log.debug("初始化Spring Application Context(spring/spring*.xml).");
            context = new ClassPathXmlApplicationContext(
                    "spring/spring*.xml");
        } catch (BeansException e) {
            log.error("初始化Spring Application Context失败(spring-*.xml).", e);
        }
    }

    /**
     * 设置是否已经初始化。<br>
     * 本方法用于从外部初始化本类，
     * 如果外部未初始化，本类的构造方法（在调用getInstance方法时调用）中会根据类路径初始化
     */
    public static void setInitialized(boolean initialized) {
        SpringConfig.initialized = initialized;
    }

    /**
     * 设置Spring ApplicationContext。<br>
     * 本方法用于从外部初始化本类，
     * 如果外部未初始化，本类的构造方法（在调用getInstance方法时调用）中会根据类路径初始化
     *
     * @param theContext Spring ApplicationContext.
     */
    public static void setApplicationContext(ApplicationContext theContext) {
        context = theContext;
    }

    /**
     * 得到Spring配置实例。
     * 单例模式.
     * @return Spring配置实例.
     */
    synchronized public static SpringConfig getInstance() {
        if (instance == null) {
            synchronized (SpringConfig.class) {
                if (instance == null) {
                    instance = new SpringConfig();
                }
            }
        }

        return instance;
    }

    /**
     * 从Spring配置文件中获取Bean对象
     * @param beanName Bean唯一标识符
     * @return
     */
    public static Object getBean(String beanName) {
        if(instance == null){
            instance = new SpringConfig();
        }
        return context.getBean(beanName);
    }

    /**
     * 获取国际化字符串
     * @param code properties中的key
     * @return 国际化字符串
     */
    public static String getString(String code) {
        return getString(code, new Object());
    }

    /**
     * 获取国际化字符串
     * @param code properties中的key
     * @param args 传入参数
     * @return 国际化字符串
     */
    public static String getString(String code, Object... args) {
        if(instance == null){
            instance = new SpringConfig();
        }
        HttpServletRequest request = RequestHelper.getRequest();
        Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
        return context.getMessage(code, args, locale);
    }
}

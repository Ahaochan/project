package com.ahao.config;

import com.ahao.exception.ConfigException;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 系统配置操作
 */
public class SystemConfig {
    private static final Logger logger = LoggerFactory.getLogger(SystemConfig.class);

    /**
     * 系统配置文件名
     */
    private static final String APPLICATION_SETTING_FILE = "application_setting.xml";

    private static final String DESCRIPTION_ATTR = "description";
    private static final String VALUE_ATTR = "value";

    /**
     * 初始化状态，true代表已初始化，否则没有初始化
     */
    private static boolean initialized;

    /**
     * “懒汉式”单例模式，避免重复初始化
     */
    private static SystemConfig systemConfig;

    private static Configuration configuration = null;

    private SystemConfig() {
        if (!initialized) { // 如果未被外界初始化，则进行初始化
            init();
            setInitialized(true);
        }
    }

    /**
     * 设置是否已经初始化。<br>
     * 本方法用于从外部初始化本类，
     * 如果外部未初始化，本类的构造方法（在调用getInstance方法时调用）中会根据类路径初始化
     */
    public static void setInitialized(boolean initialized) {
        SystemConfig.initialized = initialized;
    }

    public static synchronized SystemConfig getInstance() {
        if (systemConfig == null) {
            systemConfig = new SystemConfig();
        }
        return systemConfig;
    }

    private void init() {
        String systemPath = this.getClass().getResource("/").getPath();
        String xmlFile = systemPath + "config/" + APPLICATION_SETTING_FILE;
        try {
            logger.info("systemConfig init start...");
            configuration = loadSetting(xmlFile);
        } catch (ConfigException e) {
            logger.error("config system error:" + e.getMessage());
        }
    }


    /**
     * 提取字符串
     *
     * @param key 键值
     * @return 字符串
     */
    public String getString(String key) {
        key = getKey(key);
        return getValue(key);
    }

    /**
     * 提取单个Key元素下的所有属性值
     *
     * @param key xml元素节点
     * @return Map<String, String> Key元素下的所有属性值
     */
    public Map<String, String> getAllString(String key) {
        return getString(key, VALUE_ATTR, DESCRIPTION_ATTR);
    }

    /**
     * 提取字符串的Map集合
     *
     * @param key  元素
     * @param attr 属性数组
     * @return 元素的属性列表值
     */
    public Map<String, String> getString(String key, String... attr) {
        Map<String, String> map = new HashMap<String, String>();
        if (null == attr) {
            key = getKey(key, VALUE_ATTR);
            map.put(VALUE_ATTR, getValue(key));
        } else {
            for (int i = 0; i < attr.length; i++) {
                String newKey = getKey(key, attr[i]);
                map.put(attr[i], getValue(newKey));
            }
        }
        return map;
    }

    /**
     * 对配置文件的key进行封装
     *
     * @param key
     * @return key[@value]
     */
    private String getKey(String key) {
        return key + "[@" + VALUE_ATTR + "]";
    }

    /**
     * 封装Key
     *
     * @param key  xml中元素
     * @param attr xml中元素的属性
     * @return key[@attr] 封装后的Key
     */
    private String getKey(String key, String attr) {
        return key + "[@" + attr + "]";
    }

    /**
     * 根据key获取指定Value
     *
     * @param key 配置文件中指定的key
     * @return value 配置文件中指定的key的Value
     */
    private String getValue(String key) {
        return StringUtils.isEmpty(configuration.getString(key)) ? "" : configuration.getString(key);
    }

    /**
     * 根据key获取指定Value
     *
     * @param key 配置文件中指定的key
     * @return value 配置文件中指定的key的Value
     */
    private String[] getArrayValue(String key) {
        String[] value = null;
        if (null != configuration && null != configuration.getStringArray(key)
                && configuration.getStringArray(key).length > 0) {
            value = configuration.getStringArray(key);
        }
        return value;
    }

    private List<Object> getListValue(String key) {
        List<Object> value = null;
        if (null != configuration && null != configuration.getList(key) && configuration.getList(key).size() > 0) {
            value = configuration.getList(key);
        }
        return value;
    }

    /**
     * 提取字符串数组
     *
     * @param key 键值
     * @return 字符串数组
     */
    public String[] getStringArray(String key) {
        key = getKey(key);
        return getArrayValue(key);
    }

    /**
     * 提取字符串集合
     *
     * @param key 键值
     * @return 字符串集合
     */
    public List<Object> getList(String key) {
        key = getKey(key);
        return getListValue(key);
    }

    /**
     * 载入配置文件
     *
     * @throws ConfigException
     */
    private Configuration loadSetting(String filePath) throws ConfigException {
        File xmlFileObj = new File(filePath);
        if (!xmlFileObj.exists()) {
            logger.error("系统配置文件不存在，无法装载: " + filePath);
            return null;
        }
        logger.debug("加载系统配置文件: " + filePath);

        try {
            Configurations configs = new Configurations();
            return configs.xml(xmlFileObj);
        } catch (ConfigurationException e) {
            logger.error("读取配置文件异常: ", e);
        }
        return null;
    }

}

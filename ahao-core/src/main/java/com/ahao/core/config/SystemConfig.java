package com.ahao.core.config;

import com.ahao.core.util.lang.CollectionHelper;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
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
    private static final String ATTR_VALUE = "value";

    private Configuration configuration = null;

    private static volatile SystemConfig instance;
    public static SystemConfig instance() {
        if (instance == null) {
            synchronized (SystemConfig.class) {
                if (instance == null) {
                    instance = new SystemConfig();
                }
            }
        }
        return instance;
    }
    private SystemConfig() {
        if (configuration != null) {
            throw new IllegalStateException("Already instantiated");
        }
        init();
    }



    private void init() {
        String systemPath = this.getClass().getResource("/").getPath();
        String xmlFile = systemPath + "config/" + APPLICATION_SETTING_FILE;
        logger.info("systemConfig init start...");
        configuration = loadSetting(xmlFile);
    }

    /**
     * 获取 int 型数据
     * @param key XPath节点路径, 默认取 value 属性
     */
    public boolean getBoolean(String key){
        return getBoolean(key, ATTR_VALUE);
    }

    /**
     * 获取 boolean 型数据
     * @param key XPath节点路径
     * @param attr 指定 attr 属性, 相当于 key[@attr]
     */
    public boolean getBoolean(String key, String attr){
        key = generateKey(key, attr);
        return configuration.getBoolean(key);
    }

    /**
     * 获取 boolean 型数据
     * @param key XPath节点路径
     * @param attr 指定 attr 属性, 相当于 key[@attr]
     * @param attrs 指定 attr 属性, 相当于 key[@attr]
     */
    public Map<String, Boolean> getBoolean(String key, String attr, String... attrs){
        List<String> attrList = CollectionHelper.toList(attrs);
        attrList.add(attr);

        Map<String, Boolean> result = new HashMap<>(attrList.size());
        for (String attrItem : attrList) {
            result.put(attrItem, getBoolean(key, attrItem));
        }
        return result;
    }

    /**
     * 获取 int 型数据
     * @param key XPath节点路径, 默认取 value 属性
     */
    public int getInt(String key){
        return getInt(key, ATTR_VALUE);
    }

    /**
     * 获取 int 型数据
     * @param key XPath节点路径
     * @param attr 指定 attr 属性, 相当于 key[@attr]
     */
    public int getInt(String key, String attr){
        key = generateKey(key, attr);
        return configuration.getInt(key);
    }

    /**
     * 获取 boolean 型数据
     * @param key XPath节点路径
     * @param attr 指定 attr 属性, 相当于 key[@attr]
     * @param attrs 指定 attr 属性, 相当于 key[@attr]
     */
    public Map<String, Integer> getInt(String key, String attr, String... attrs){
        List<String> attrList = CollectionHelper.toList(attrs);
        attrList.add(attr);

        Map<String, Integer> result = new HashMap<>(attrList.size());
        for (String attrItem : attrList) {
            result.put(attrItem, getInt(key, attrItem));
        }
        return result;
    }

    /**
     * 获取 String 型数据
     * @param key XPath节点路径, 默认取 value 属性
     */
    public String getString(String key) {
        return getString(key, ATTR_VALUE);
    }

    /**
     * 获取 String 型数据
     * @param key XPath节点路径
     * @param attr 指定 attr 属性, 相当于 key[@attr]
     */
    public String getString(String key, String attr){
        key = generateKey(key, attr);
        return configuration.getString(key);
    }

    /**
     * 获取 String 型数据
     * @param key XPath节点路径
     * @param attr 指定 attr 属性, 相当于 key[@attr]
     * @param attrs 指定 attr 属性, 相当于 key[@attr]
     */
    public Map<String, String> getString(String key, String attr, String... attrs){
        List<String> attrList = CollectionHelper.toList(attrs);
        attrList.add(attr);

        Map<String, String> result = new HashMap<>(attrList.size());
        for (String attrItem : attrList) {
            result.put(attrItem, getString(key, attrItem));
        }
        return result;
    }

    /**
     * 封装Key
     *
     * @param key  xml中元素
     * @param attr xml中元素的属性
     * @return key[@attr] 封装后的Key
     */
    private String generateKey(String key, String attr) {
        return key + "[@" + attr + "]";
    }

    /**
     * 载入配置文件
     */
    private Configuration loadSetting(String filePath) {
        File xmlFile = new File(filePath);
        if (!xmlFile.exists()) {
            logger.error("系统配置文件不存在，无法装载: " + filePath);
            return null;
        }
        logger.debug("加载系统配置文件: " + filePath);
        try {
            XMLConfiguration xmlConfiguration = new XMLConfiguration(filePath);
            xmlConfiguration.setReloadingStrategy(new FileChangedReloadingStrategy());
            xmlConfiguration.setAutoSave(true);
            return xmlConfiguration;
        } catch (ConfigurationException e) {
            logger.error("读取配置文件异常: ", e);
        }
        return null;
    }

}
package com.ahao.config;


import com.ahao.context.ApplicationContext;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 系统配置文件操作。<br>
 * 可以从系统配置文件中读取指定属性的值。
 * 
 * @author 邓坤
 */
public class Setter {
	// TODO 改造

	public Setter() {

	}

	/**
	 * 提取字符串
	 * 
	 * @param key 键值
	 * @return 字符串
	 */
	public static String getString(String key) {
		return SystemConfig.getInstance().getString(key);
	}

	/**
	 * 提取单个Key元素下的所有属性值
	 * 
	 * @param key xml元素节点
	 * @return Map<String, String> Key元素下的所有属性值
	 */
	public static Map<String, String> getAllString(String key) {
		return SystemConfig.getInstance().getAllString(key);
	}

	/**
	 * 提取配置文件中boolean类型值
	 * 
	 * @param key
	 * @return true/false
	 */
	public static boolean getBoolean(String key) {
		return Boolean.valueOf(getString(key)).booleanValue();
	}

	/**
	 * 提取配置文件中整型值
	 * 
	 * @param key
	 * @return int
	 */
	public static int getInt(String key) {
		return Integer.parseInt(getString(key));
	}

	/**
	 * 提取系统的路径
	 * 
	 * @return 系统的路径
	 */
	public static String getSystemRoot() {
		String path = ApplicationContext.getSystemRoot();
		return StringUtils.replace(path, "\\", "/");
	}

	/**
	 * 提取字符串数组
	 * 
	 * @param key 键值
	 * @return 字符串数组
	 */
	public static String[] getStringArray(String key) {
		return SystemConfig.getInstance().getStringArray(key);
	}

	/**
	 * 提取字符串集合
	 * 
	 * @param key 键值
	 * @return 字符串集合
	 */
	public static List<Object> getList(String key) {
		return SystemConfig.getInstance().getList(key);
	}
}

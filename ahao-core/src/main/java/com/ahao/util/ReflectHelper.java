package com.ahao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Created by Ahaochan on 2017/8/10.
 * 反射工具类
 */
@SuppressWarnings("unchecked")
public abstract class ReflectHelper {
    private static final Logger logger = LoggerFactory.getLogger(ReflectHelper.class);
    private static final int DEFAULT_ARRAY_LENGTH = 16;


    /**
     * 反射创建无参实例
     */
    public static <T> T create(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            logger.error("不能实例化" + clazz.getName() + ", 请查看该类是否为无法被实例化的类, 如接口, 抽象类, 或者没有无参构造函数: ", e);
        } catch (IllegalAccessException e) {
            logger.error("不能访问" + clazz.getName() + ", 请查看该类的构造函数是否为private: ", e);
        }
        return null;
    }

    /**
     * 反射创建一个数组, 默认长度为16
     *
     * @param clazz 类型
     * @return 数组
     */
    public static <T> T[] createArray(Class<T> clazz) {
        return createArray(clazz, DEFAULT_ARRAY_LENGTH);
    }

    /**
     * 反射创建一个数组
     *
     * @param clazz  类型
     * @param length 数组长度
     * @return 数组
     */
    public static <T> T[] createArray(Class<T> clazz, int length) {
        return (T[]) Array.newInstance(clazz, length);
    }

    /**
     * 获取 collection 集合中子元素的泛型类型
     *
     * @param collection 集合
     * @return Class类
     */
    public static <T> Class<T> getElementClass(Collection<T> collection) {
        if (CollectionHelper.isEmpty(collection)) {
            return null;
        }
        return (Class<T>) collection.iterator().next().getClass();
    }

    /**
     * 获取数组中子元素的泛型类型
     *
     * @param array 数组
     * @return Class类
     */
    public static <T> Class<T> getElementClass(T... array) {
        if (ArrayHelper.isEmpty(array)) {
            return null;
        }
        return (Class<T>) array[0].getClass();
    }
}
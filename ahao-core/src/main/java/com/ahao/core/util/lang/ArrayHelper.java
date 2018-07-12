package com.ahao.core.util.lang;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Ahaochan on 2017/8/10.
 */
@SuppressWarnings("unchecked")
public abstract class ArrayHelper {
    private static final Logger logger = LoggerFactory.getLogger(ArrayHelper.class);

    private ArrayHelper() {
        throw new AssertionError("工具类不允许实例化");
    }

    /**
     * 将 collection 转化为数组
     *
     * @param collection 集合
     * @return 数组
     */
    public static <T> T[] toArray(Collection<T> collection) {
        return toArray(collection, 0, 0);
    }

    /**
     * 将 collection 转化为数组
     *
     * @param collection 集合
     * @param offset     起始偏移，小于等于0代表不偏移。如果偏移大于数组长度，则返回空列表.
     * @param limit      要转换的记录数，不能大于数组长度，小于等于0代表不限制
     * @return 数组
     */
    public static <T> T[] toArray(Collection<T> collection, int offset, int limit) {
        if (CollectionHelper.isEmpty(collection) || offset > collection.size()) {
            return (T[]) new Object[0];
        }

        // 反射获取元素Class
        Class<T> clazz = ReflectHelper.getElementClass(collection);
        // 反射创建数组
        T[] array = ReflectHelper.createArray(clazz, collection.size());

        Iterator<T> iterator = collection.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            array[index++] = iterator.next();
        }

        offset = NumberUtils.max(0, offset);
        int end = limit == 0 ? collection.size() : offset + limit;
        return ArrayUtils.subarray(array, offset, end);
    }

    /**
     * 快速创建数组
     * @param elements 数组元素
     */
    public static <T> T[] toArray(T... elements) {
        return elements;
    }

    /**
     * 获取数组长度, 防止空指针
     * @param array 数组
     */
    public static <T> int length(T... array){
        return array == null ? 0 : array.length;
    }

    /**
     * 将数组转化为 String 类型
     * @param elements 数组
     * @return 字符串
     */
    public static <T> String toString(T... elements) {
        return ArrayUtils.toString(elements);
    }
}
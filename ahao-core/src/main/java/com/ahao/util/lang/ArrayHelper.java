package com.ahao.util.lang;

import com.ahao.util.lang.math.NumberHelper;
import org.apache.commons.lang3.ArrayUtils;
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
     * 判断数组是否为空
     *
     * @param elements 数组
     * @param <T>   泛型
     * @return true为空数组
     */
    public static <T> boolean isEmpty(T[] elements) {
        return ArrayUtils.isEmpty(elements);
    }

    public static <T> boolean isNotEmpty(T[] elements) {
        return !isEmpty(elements);
    }

    /**
     * array数组是否包含item元素
     *
     * @param array 数组
     * @param item
     * @return
     */
    public static <T> boolean contains(T[] array, T item) {
        return ArrayUtils.contains(array, item);
    }

    public static <T> T get(int index, T... array) {
        if (ArrayHelper.isNotEmpty(array) && array.length > index) {
            return array[index];
        }
        return null;
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
            return null;
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

        offset = NumberHelper.max(0, offset);
        int end = limit == 0 ? collection.size() : offset + limit;
        return ArrayUtils.subarray(array, offset, end);
    }

    /**
     * 返回子数组, 兼容越界情况
     *
     * @param array 数组
     * @param startIndexInclusive 开始位置, 包括
     * @param endIndexExclusive   结束位置, 不包括
     * @return 子集合
     */
    public static <T> T[] subArray(T[] array, int startIndexInclusive, int endIndexExclusive) {
        return ArrayUtils.subarray(array, startIndexInclusive, endIndexExclusive);
    }
    public static <T> T[] subArray(T[] array, int startIndexInclusive) {
        return ArrayUtils.subarray(array, startIndexInclusive, length(array));
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
     *
     * @param elements 数组
     * @return 字符串
     */
    public static <T> String toString(T... elements) {
        return ArrayUtils.toString(elements);
    }
}
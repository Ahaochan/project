package com.ahao.util;

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

    private ArrayHelper(){
    }

    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @param <T>   泛型
     * @return true为空数组
     */
    public static <T> boolean isEmpty(T[] array) {
        return ArrayUtils.isEmpty(array);
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
     * 将 elements 转化为数组
     *
     * @param elements 数组
     * @return 数组
     */
    public static <T> T[] toArray(T... elements) {
        return ArrayUtils.toArray(elements);
    }



    /**
     * 把Collection的数据转化为整形数组
     *
     * @param coll 需要转化的集合，里面必须全部存放Integer型对象，否则尝试将该对象的toString方法返回的字符串转换为整形数，
     *             无法转换用0代替.
     * @return 数字数组
     */
    public static <T> int[] toIntArray(Collection<T> coll) {
        return toIntArray(coll, 0, 0);
    }

    /**
     * 把Collection的数据转化为整形数组
     *
     * @param coll   需要转化的集合，里面必须全部存放Integer型对象，否则尝试将该对象的toString方法返回的字符串转换为整形数，
     *               无法转换用0代替.
     * @param offset 起始偏移，小于等于0代表不偏移。如果偏移大于数组长度，则返回空列表.
     * @param limit  要转换的记录数，不能大于数组长度，小于等于0代表不限制
     * @return 数字数组
     */
    public static <T> int[] toIntArray(Collection<T> coll, int offset, int limit) {
        T[] array = toArray(coll, offset, limit);
        // 集合为空则返回空数组
        if (isEmpty(array)) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        }

        int[] result = new int[array.length];
        int index = 0;
        for (T item : array) {
            String str = item.toString();
            // 无法转换用0代替.
            result[index++] = StringHelper.isNumeric(str) ? Integer.parseInt(str) : 0;
        }
        return result;
    }

    /**
     * 把Collection的数据转化为字符串数组
     *
     * @param coll 需要转化的集合，当集合中包含了空对象时，则当作""。否则调用对象的toString方法并添加至结果数组.
     * @return 字符串数组
     */
    public static <T> String[] toStringArray(Collection<T> coll) {
        return toStringArray(coll, 0, 0);
    }

    /**
     * 把Collection的数据转化为整形数组
     *
     * @param coll   需要转化的集合，当集合中包含了空对象时，则当作""。否则调用对象的toString方法并添加至结果数组.
     * @param offset 起始偏移，小于等于0代表不偏移。如果偏移大于数组长度，则返回空列表.
     * @param limit  要转换的记录数，不能大于数组长度，小于等于0代表不限制
     * @return 字符串数组
     */
    public static <T> String[] toStringArray(Collection<T> coll, int offset, int limit) {
        T[] array = toArray(coll, offset, limit);
        // 集合为空则返回空数组
        if (isEmpty(array)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        String[] result = new String[array.length];
        int index = 0;
        for (T item : array) {
            // 为空用""代替
            result[index++] = (item == null) ? "" : item.toString();
        }
        return result;
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
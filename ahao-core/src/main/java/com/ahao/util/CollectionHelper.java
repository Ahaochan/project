package com.ahao.util;

import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * Created by Ahaochan on 2017/8/6.
 * 集合的操作工具类
 */
public abstract class CollectionHelper {

    /**
     * 返回 collection 集合是否为空
     *
     * @param collection 集合
     * @return 当集合为null, 或没有元素时返回true, 否则返回false
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 返回 map 集合是否为空
     *
     * @param map 集合
     * @return 当集合为null, 或没有元素时返回true, 否则返回false
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 返回 enumeration 集合是否为空
     *
     * @param enumeration 集合
     * @return 当集合为null, 或没有元素时返回true, 否则返回false
     */
    public static boolean isEmpty(Enumeration<?> enumeration) {
        return enumeration == null || enumeration.hasMoreElements();
    }

    public static boolean isNotEmpty(Enumeration<?> enumeration) {
        return !isEmpty(enumeration);
    }

    /**
     * 往 collection 集合中添加元素
     *
     * @param collection 集合
     * @param elements   变长数组的元素
     * @param <T>        集合元素类型
     * @return 任意一个添加成功返回true
     */
    @SafeVarargs
    public static <T> boolean add(Collection<T> collection, T... elements) {
        return collection != null && Collections.addAll(collection, elements);
    }

    /**
     * 将数组元素变为 List 集合类型
     *
     * @param elements 所有元素
     * @return 集合, 一般为 ArrayList
     */
    public static <T> List<T> toList(T[] elements) {
        if (ArrayHelper.isEmpty(elements)) {
            return null;
        }
        List<T> list = new ArrayList<>(elements.length);
        add(list, elements);
        return list;
    }

    /**
     * 将 数组 转化为 collection
     *
     * @param elements 数组
     * @param offset   起始偏移，小于等于0代表不偏移。如果偏移大于数组长度，则返回空列表.
     * @param limit    要转换的记录数，不能大于数组长度，小于等于0代表不限制
     * @return 数组
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(T[] elements, int offset, int limit) {
        if (ArrayHelper.isEmpty(elements) || offset > elements.length) {
            return new ArrayList<>();
        }

        offset = NumberHelper.max(0, offset);
        int end = limit == 0 ? elements.length : offset + limit;

        return toList(ArrayUtils.subarray(elements,
                offset, end));
    }

    /**
     * 将数组元素变为 Set 集合类型
     *
     * @param elements 所有元素
     * @return 集合, 一般为 HashSet
     */
    public static <T> Set<T> toSet(T... elements) {
        Set<T> set = new HashSet<>(elements.length);
        add(set, elements);
        return set;
    }



    /**
     * 往 collection 集合中添加元素elements
     *
     * @param collection 集合
     * @param elements   元素
     * @return 若集合为null, 返回false, 若集合中存在元素, 返回true, 若集合中不存在元素, 返回false
     */
    public static <T> boolean contains(Collection<? super T> collection, T elements) {
        return !isEmpty(collection) && collection.contains(elements);
    }

    /**
     * 打印输出集合中的元素
     *
     * @param collection 集合
     */
    public static <T> void print(Collection<T> collection) {
        if (isEmpty(collection)) {
            return;
        }
        System.out.println("元素类型为:" + collection.iterator().next().getClass());
        StringBuilder sb = new StringBuilder();
        for (T item : collection) {
            sb.append(item).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb.toString());
    }
}

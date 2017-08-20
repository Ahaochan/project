package com.ahao.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by Ahaochan on 2017/8/6.
 */
public abstract class CollectionHelper {

    /**
     * 判断 Enumeration 集合是否为空
     *
     * @param enumeration 集合
     * @return true为空, false为非空
     */
    public static <T> boolean isEmpty(Enumeration<? super T> enumeration) {
        return enumeration == null || enumeration.hasMoreElements();
    }

    /**
     * 判断 Collection 集合是否为空
     *
     * @param collection 集合
     * @return true为空, false为非空
     */
    public static <T> boolean isEmpty(Collection<? super T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断 Map 集合是否为空
     *
     * @param map 集合
     * @return true为空, false为非空
     */
    public static <K, V> boolean isEmpty(Map<? super K, ? super V> map) {
        return map == null || map.isEmpty();
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
     * 返回 Long 数组
     *
     * @param collection 集合
     * @return 数组
     */
    public static Long[] toLongArray(Collection<String> collection) {
        return collection.stream().map(Long::parseLong).toArray(Long[]::new);
    }
}

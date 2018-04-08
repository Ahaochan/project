package com.ahao.core.util.lang;

import com.ahao.core.util.lang.math.NumberHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * Created by Ahaochan on 2017/8/6.
 * 集合的操作工具类
 */
public abstract class CollectionHelper {

    private CollectionHelper() {
        throw new AssertionError("工具类不允许实例化");
    }

    /**
     * 返回集合是否为空
     *
     * @param collection 集合
     * @return 当集合为null, 或没有元素时返回true, 否则返回false
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
    public static boolean isEmpty(Enumeration<?> enumeration) {
        return enumeration == null || enumeration.hasMoreElements();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
    public static boolean isNotEmpty(Enumeration<?> enumeration) {
        return !isEmpty(enumeration);
    }

    /**
     * 获取集合长度, 防止空指针
     */
    public static int size(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }
    public static int size(Map<?, ?> map) {
        return map == null ? 0 : map.size();
    }
    public static int size(Enumeration<?> enumeration) {
        return enumeration == null ? 0 : CollectionUtils.size(enumeration);
    }

    /**
     * 返回空集合, 避免返回null, 导致NPE
     */
    public static <T> List<T> emptyList(){
        return Collections.emptyList();
    }
    public static <K,V> Map<K,V> emptyMap(){
        return Collections.emptyMap();
    }
    public static <T> Set<T> emptySet(){
        return Collections.emptySet();
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
            return new ArrayList<>();
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
     * 返回子集合, 兼容越界情况
     *
     * @param list                集合
     * @param startIndexInclusive 开始位置, 包括
     * @param endIndexExclusive   结束位置, 不包括
     * @return 子集合
     */
    public static <T> List<T> subList(List<T> list, int startIndexInclusive, int endIndexExclusive) {
        return list.subList(Math.max(0, startIndexInclusive), Math.min(list.size(), endIndexExclusive));
    }

    /**
     * collection 集合是否存在元素 elements
     *
     * @param collection 集合
     * @param elements   元素
     * @return 若集合为null, 返回false, 若集合中存在元素, 返回true, 若集合中不存在元素, 返回false
     */
    public static <T> boolean contains(Collection<? super T> collection, T elements) {
        return !isEmpty(collection) && collection.contains(elements);
    }

    /**
     * 对List中的Map进行压缩, 只保留keys中的键值对
     * @param maps 集合
     * @param keys 键
     * @return 压缩后的Map
     */
    public static <M extends Map<K, V>, K, V> List<M> retain(List<M> maps, K... keys) {
        @SuppressWarnings("unchecked")
        List<M> list = ReflectHelper.create(maps.getClass());
        if (isEmpty(maps) || list == null) {
            return null;
        }
        for (M map : maps) {
            map.keySet().retainAll(toList(keys));
            list.add(map);
        }
        return list;
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

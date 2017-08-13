package com.ahao.util;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ahaochan on 2017/8/10.
 */
public abstract class ArrayHelper {

    public static <T> T[] toArray(T... elements) {
        return ArrayUtils.toArray(elements);
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length <= 0;
    }

    public static <T> Long[] toArray(String... elements) {
        return Arrays.stream(elements).map(Long::parseLong).toArray(Long[]::new);
    }

    /**
     * 将元素数组转为集合
     *
     * @param elements 元素
     * @return 集合
     */
    public static <T> List<T> toList(T... elements) {
        return Arrays.stream(elements).collect(Collectors.toList());
    }
}

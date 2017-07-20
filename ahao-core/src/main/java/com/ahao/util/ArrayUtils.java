package com.ahao.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 数组工具类
 *
 * @author cleven_lee
 */
public class ArrayUtils {
    static int[] EMPTY_INT_ARRAY = new int[0];
    static String[] EMPTY_STRING_ARRAY = new String[0];

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> list) {
        return toArray(list, 0, 0);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> list, int offset, int limit) {
        return (T[]) list.stream()
                .skip(offset >= 0 ? offset : 0)
                .limit(limit > 0 ? limit : Integer.MAX_VALUE)
                .toArray();
    }
}
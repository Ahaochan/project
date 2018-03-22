package com.ahao.util.lang.math;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ahaochan on 2017/8/1.
 *
 * 数字操作的工具类
 */
public abstract class NumberHelper {
    private static final Logger logger = LoggerFactory.getLogger(NumberHelper.class);

    private NumberHelper() {
        throw new AssertionError("工具类不允许实例化");
    }

    /**
     * num 是否在 [min, max] 之间
     *
     * @param num 当前值
     * @param min 最小值下限
     * @param max 最大值上限
     * @return num 是否在 [min, max] 之间
     */
    public static boolean isBetween(int num, int min, int max) {
        return num >= min && num <= max;
    }
    public static boolean isBetween(long num, long min, long max) {
        return num >= min && num <= max;
    }

    public static int max(int... array) {
        return NumberUtils.max(array);
    }
    public static long max(long... array) {
        return NumberUtils.max(array);
    }
    public static float max(float... array) {
        return NumberUtils.max(array);
    }
    public static double max(double... array) {
        return NumberUtils.max(array);
    }

    public static int min(int... array) {
        return NumberUtils.min(array);
    }
    public static long min(long... array) {
        return NumberUtils.min(array);
    }
    public static float min(float... array) {
        return NumberUtils.min(array);
    }
    public static double min(double... array) {
        return NumberUtils.min(array);
    }


    /**
     * 数组拆箱
     *
     * @param array 包装类数组
     * @return 基本数据类型数组
     */
    public static int[] unboxing(Integer... array) {
        int len = array.length;
        int[] result = new int[len];
        for(int i = 0; i < len; i++){
            result[i] = array[i] == null ? 0 : array[i];
        }
        return result;
    }
    public static long[] unboxing(Long... array) {
        int len = array.length;
        long[] result = new long[len];
        for(int i = 0; i < len; i++){
            result[i] = array[i] == null ? 0 : array[i];
        }
        return result;
    }
    public static float[] unboxing(Float... array) {
        int len = array.length;
        float[] result = new float[len];
        for(int i = 0; i < len; i++){
            result[i] = array[i] == null ? 0 : array[i];
        }
        return result;
    }
    public static double[] unboxing(Double... array) {
        int len = array.length;
        double[] result = new double[len];
        for(int i = 0; i < len; i++){
            result[i] = array[i] == null ? 0 : array[i];
        }
        return result;
    }

    /**
     * 数组装箱
     *
     * @param array 基本数据类型数组
     * @return 包装类数组
     */
    public static Integer[] enboxing(int... array) {
        Integer[] result = new Integer[array.length];
        int index = 0;
        for (Integer num : array) {
            result[index++] = num;
        }
        return result;
    }
    public static Long[] enboxing(long... array) {
        Long[] result = new Long[array.length];
        int index = 0;
        for (Long num : array) {
            result[index++] = num;
        }
        return result;
    }
    public static Float[] enboxing(float... array) {
        Float[] result = new Float[array.length];
        int index = 0;
        for (Float num : array) {
            result[index++] = num;
        }
        return result;
    }
    public static Double[] enboxing(double... array) {
        Double[] result = new Double[array.length];
        int index = 0;
        for (Double num : array) {
            result[index++] = num;
        }
        return result;
    }

    public static <T> boolean isNumber(T obj) {
        return obj != null && NumberUtils.isCreatable(obj.toString());
    }

    public static int between(int min, int max, int number) {
        if (min > max) {
            max = min;
        }

        if (number < min) {
            return min;
        } else if (number > max) {
            return max;
        } else {
            return number;
        }
    }


    public static int parseInt(Object obj) {
        try {
            if (obj instanceof Boolean) {
                return Boolean.valueOf(obj.toString()) ? 1 : 0;
            }
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            logger.error("解析数字" + obj + "失败:", e);
        }
        return 0;
    }

}
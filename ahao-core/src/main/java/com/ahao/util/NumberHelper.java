package com.ahao.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ahaochan on 2017/8/1.
 */
public abstract class NumberHelper {
    private static final Logger logger = LoggerFactory.getLogger(NumberHelper.class);

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

    public static int[] unboxing(Integer... array) {
        int[] result = new int[array.length];
        int index = 0;
        for (int num : array) {
            result[index++] = num;
        }
        return result;
    }
    public static long[] unboxing(Long... array) {
        long[] result = new long[array.length];
        int index = 0;
        for (long num : array) {
            result[index++] = num;
        }
        return result;
    }
    public static float[] unboxing(Float... array) {
        float[] result = new float[array.length];
        int index = 0;
        for (float num : array) {
            result[index++] = num;
        }
        return result;
    }
    public static double[] unboxing(Double... array) {
        double[] result = new double[array.length];
        int index = 0;
        for (double num : array) {
            result[index++] = num;
        }
        return result;
    }

    public static boolean unBoxing(Boolean bool) {
        return bool == null ? false : bool;
    }
    public static int unBoxing(Integer num) {
        return num == null ? 0 : num;
    }

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
            if (obj instanceof Number) {
                return Integer.parseInt(obj.toString());
            }
        } catch (NumberFormatException e) {
            logger.error("解析数字" + obj + "失败:", e);
        }
        return 0;
    }

}

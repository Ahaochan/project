package com.ahao.util;

import com.ibm.icu.text.NumberFormat;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * Created by Ahaochan on 2017/8/1.
 */
public abstract class NumberHelper {
    private static final Logger logger = LoggerFactory.getLogger(NumberHelper.class);
    private static final Locale hans = new Locale("C@numbers=hans");
    private static final Locale hant = new Locale("C@numbers=hant");
    private static final Locale hansfin = new Locale("C@numbers=hansfin");

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

    /**
     * num 是否在 [min, max] 之间
     *
     * @param num 当前值
     * @param min 最小值下限
     * @param max 最大值上限
     * @return num 是否在 [min, max] 之间
     */
    public static boolean isBetween(long num, long min, long max) {
        return num >= min && num <= max;
    }

    /**
     * 找到数组中的最大值
     *
     * @param array 数组
     * @return 最大值
     */
    public static byte max(byte... array) {
        return NumberUtils.max(array);
    }

    /**
     * 找到数组中的最大值
     *
     * @param array 数组
     * @return 最大值
     */
    public static short max(short... array) {
        return NumberUtils.max(array);
    }

    /**
     * 找到数组中的最大值
     *
     * @param array 数组
     * @return 最大值
     */
    public static int max(int... array) {
        return NumberUtils.max(array);
    }

    /**
     * 找到数组中的最大值
     *
     * @param array 数组
     * @return 最大值
     */
    public static long max(long... array) {
        return NumberUtils.max(array);
    }

    /**
     * 找到数组中的最大值
     *
     * @param array 数组
     * @return 最大值
     */
    public static float max(float... array) {
        return NumberUtils.max(array);
    }

    /**
     * 找到数组中的最大值
     *
     * @param array 数组
     * @return 最大值
     */
    public static double max(double... array) {
        return NumberUtils.max(array);
    }

    /**
     * 找到数组中的最小值
     *
     * @param array 数组
     * @return 最小值
     */
    public static byte min(byte... array) {
        return NumberUtils.min(array);
    }

    /**
     * 找到数组中的最小值
     *
     * @param array 数组
     * @return 最小值
     */
    public static short min(short... array) {
        return NumberUtils.min(array);
    }

    /**
     * 找到数组中的最小值
     *
     * @param array 数组
     * @return 最小值
     */
    public static int min(int... array) {
        return NumberUtils.min(array);
    }

    /**
     * 找到数组中的最小值
     *
     * @param array 数组
     * @return 最小值
     */
    public static long min(long... array) {
        return NumberUtils.min(array);
    }

    /**
     * 找到数组中的最小值
     *
     * @param array 数组
     * @return 最小值
     */
    public static float min(float... array) {
        return NumberUtils.min(array);
    }

    /**
     * 找到数组中的最小值
     *
     * @param array 数组
     * @return 最小值
     */
    public static double min(double... array) {
        return NumberUtils.min(array);
    }

    /**
     * 数组拆箱
     *
     * @param array
     * @return
     */
    public static byte[] unboxing(Byte... array) {
        byte[] result = new byte[array.length];
        int index = 0;
        for (byte num : array) {
            result[index++] = num;
        }
        return result;
    }

    /**
     * 数组拆箱
     *
     * @param array 包装类数组
     * @return 基本数据类型数组
     */
    public static short[] unboxing(Short... array) {
        short[] result = new short[array.length];
        int index = 0;
        for (short num : array) {
            result[index++] = num;
        }
        return result;
    }

    /**
     * 数组拆箱
     *
     * @param array 包装类数组
     * @return 基本数据类型数组
     */
    public static int[] unboxing(Integer... array) {
        int[] result = new int[array.length];
        int index = 0;
        for (int num : array) {
            result[index++] = num;
        }
        return result;
    }

    /**
     * 数组拆箱
     *
     * @param array 包装类数组
     * @return 基本数据类型数组
     */
    public static long[] unboxing(Long... array) {
        long[] result = new long[array.length];
        int index = 0;
        for (long num : array) {
            result[index++] = num;
        }
        return result;
    }

    /**
     * 数组拆箱
     *
     * @param array 包装类数组
     * @return 基本数据类型数组
     */
    public static float[] unboxing(Float... array) {
        float[] result = new float[array.length];
        int index = 0;
        for (float num : array) {
            result[index++] = num;
        }
        return result;
    }

    /**
     * 数组拆箱
     *
     * @param array 包装类数组
     * @return 基本数据类型数组
     */
    public static double[] unboxing(Double... array) {
        double[] result = new double[array.length];
        int index = 0;
        for (double num : array) {
            result[index++] = num;
        }
        return result;
    }

    /**
     * 拆箱
     *
     * @param num
     * @return
     */
    public static int unBoxing(Integer num) {
        return num == null ? 0 : num;
    }

    /**
     * 数组装箱
     *
     * @param array 基本数据类型数组
     * @return 包装类数组
     */
    public static Byte[] enboxing(byte... array) {
        Byte[] result = new Byte[array.length];
        int index = 0;
        for (Byte num : array) {
            result[index++] = num;
        }
        return result;
    }

    /**
     * 数组装箱
     *
     * @param array 基本数据类型数组
     * @return 包装类数组
     */
    public static Short[] enboxing(short... array) {
        Short[] result = new Short[array.length];
        int index = 0;
        for (Short num : array) {
            result[index++] = num;
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

    /**
     * 数组装箱
     *
     * @param array 基本数据类型数组
     * @return 包装类数组
     */
    public static Long[] enboxing(long... array) {
        Long[] result = new Long[array.length];
        int index = 0;
        for (Long num : array) {
            result[index++] = num;
        }
        return result;
    }

    /**
     * 数组装箱
     *
     * @param array 基本数据类型数组
     * @return 包装类数组
     */
    public static Float[] enboxing(float... array) {
        Float[] result = new Float[array.length];
        int index = 0;
        for (Float num : array) {
            result[index++] = num;
        }
        return result;
    }

    /**
     * 数组装箱
     *
     * @param array 基本数据类型数组
     * @return 包装类数组
     */
    public static Double[] enboxing(double... array) {
        Double[] result = new Double[array.length];
        int index = 0;
        for (Double num : array) {
            result[index++] = num;
        }
        return result;
    }

    /**
     * @param min 区间下限
     * @param max 区间上限
     * @param number 区间内的值
     * @return 返回number在[min,max]之间的映射
     */
    public static int between(int min, int max, int number) {
        if(min>max){
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

    /**
     * 将数字转为简体汉字
     * 61305 -> 六万一千三百零五
     *
     * @param num 数字
     * @return 简体汉字
     */
    public static String hans(Integer num) {
        return hans(num + "");
    }

    /**
     * 将数字转为简体汉字
     * 61305 -> 六万一千三百零五
     *
     * @param num 数字
     * @return 简体汉字
     */
    public static String hans(String num) {
        return NumberFormat.getInstance(hans).format(num);
    }

    /**
     * 将数字转为繁体汉字
     * 61305 -> 六萬一千三百零五
     *
     * @param num 数字
     * @return 繁体汉字
     */
    public static String hant(Integer num) {
        return hant(num + "");
    }

    /**
     * 将数字转为繁体汉字
     * 61305 -> 六萬一千三百零五
     *
     * @param num 数字
     * @return 繁体汉字
     */
    public static String hant(String num) {
        return NumberFormat.getInstance(hant).format(num);
    }

    /**
     * 将数字转为金融类繁体汉字
     * 61305 -> 陆万壹仟叁佰零伍
     *
     * @param num 数字
     * @return 金融类繁体汉字
     */
    public static String hantfin(String num) {
        return hansfin(num + "");
    }

    /**
     * 将数字转为金融类繁体汉字
     * 61305 -> 陆万壹仟叁佰零伍
     *
     * @param num 数字
     * @return 金融类繁体汉字
     */
    public static String hansfin(String num) {
        return NumberFormat.getInstance(hansfin).format(num);
    }


    /**
     * 将Object解析为int型数据
     * @param obj
     * @return
     */
    public static int parse(Object obj) {
        try{
            if(obj instanceof Boolean){
                return Boolean.valueOf(obj.toString()) ? 1 : 0;
            }
            if(obj instanceof Number){
                return Integer.parseInt(obj.toString());
            }
        } catch (NumberFormatException e){
            logger.error("解析数字"+obj+"失败:",e);
        }
        return 0;
    }

}

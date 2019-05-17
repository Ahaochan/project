package com.ahao.util.commons.lang;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ahaochan on 2017/8/18.
 * <p>
 * 随机数工具类
 */
public class RandomHelper {
    // 数字数据字典
    public static final String DIST_NUMBER = "0123456789";
    // 小写字母数据字典
    public static final String DIST_LETTER_LOWER = "abcdefghijklmnopqrstuvwxyz";
    // 大写字母数据字典
    public static final String DIST_LETTER_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // 不区分大小写的字母数据字典
    public static final String DIST_LETTER = DIST_LETTER_LOWER + DIST_LETTER_UPPER;
    // 数字+字母的数据字典
    public static final String DIST_NUMBER_LETTER = DIST_NUMBER + DIST_LETTER;

    /**
     * 生成[0, endExclusive) 区间内的随机数
     *
     * @param endExclusive 上限, 不包含该上限
     * @return 随机数
     */
    public static int getInt(int endExclusive) {
        return getInt(0, endExclusive);
    }

    /**
     * 生成[startInclusive, endExclusive) 区间内的随机数
     *
     * @param startInclusive 下限, 包含该下限
     * @param endExclusive   上限, 不包含该上限
     * @return 随机数
     */
    public static int getInt(int startInclusive, int endExclusive) {
        if (startInclusive > endExclusive) {
            throw new IndexOutOfBoundsException("随机数的最小值必须小于最大值");
        }

        if (startInclusive == endExclusive) {
            return startInclusive;
        }
        return startInclusive + ThreadLocalRandom.current().nextInt(endExclusive - startInclusive);
    }

    /**
     * 从 origin 的字符串中随机获取 size 个字符并返回
     *
     * @param size   返回字符串的大小
     * @param origin 源字符字典
     * @return 随机的字符串
     */
    public static String getString(int size, String origin) {
        return new RandomStringGenerator.Builder()
                .selectFrom(StringUtils.isEmpty(origin) ? new char[0] : origin.toCharArray())
                .build()
                .generate(size);
    }

    public static String getString(String origin) {
        return getString(getInt(origin.length()), origin);
    }

    /**
     * 随机生成一个rgb颜色,
     *
     * @param red   红原色
     * @param green 绿原色
     * @param blue  蓝原色
     * @return 颜色
     */
    public static Color getColor(int red, int green, int blue) {
        return new Color(getInt(red), getInt(green), getInt(blue));
    }

}
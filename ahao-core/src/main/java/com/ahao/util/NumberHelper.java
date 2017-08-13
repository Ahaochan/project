package com.ahao.util;

import com.ibm.icu.text.NumberFormat;

import java.util.Locale;

/**
 * Created by Ahaochan on 2017/8/1.
 */
public abstract class NumberHelper {
    private static final Locale hans = new Locale("C@numbers=hans");
    private static final Locale hant = new Locale("C@numbers=hant");
    private static final Locale hansfin = new Locale("C@numbers=hansfin");


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
     * 拆箱
     *
     * @param num
     * @return
     */
    public static int unBoxing(Integer num) {
        return num == null ? 0 : num;
    }

    public static int parse(boolean bool) {
        return bool ? 1 : 0;
    }
}

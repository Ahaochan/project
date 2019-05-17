package com.ahao.util.commons.lang.math;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ahaochan on 2017/8/1.
 *
 * 数字操作的工具类
 */
public class NumberHelper {
    private static final Logger logger = LoggerFactory.getLogger(NumberHelper.class);

    /**
     * num 是否在 [min, max] 之间
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

    public static int between(int number, int min, int max) {
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

    public static <T> boolean isNumber(T obj) {
        return obj != null && NumberUtils.isCreatable(obj.toString());
    }
    public static <T> boolean isInteger(T obj) {
        return obj != null && StringUtils.isNumeric(obj.toString());
    }

    public static int parseInt(Object obj) {
        if(obj == null) {
            return 0;
        }
        try {
            if (obj instanceof Boolean) {
                return Boolean.valueOf(obj.toString()) ? 1 : 0;
            }
            if(!NumberUtils.isCreatable(obj.toString())) {
                return 0;
            }
            return (int) Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
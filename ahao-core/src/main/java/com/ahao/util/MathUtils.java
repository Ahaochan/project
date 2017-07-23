package com.ahao.util;

/**
 * Created by Avalon on 2017/5/23.
 *
 * 数学的工具类
 */
public class MathUtils {
    private MathUtils() {
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

}

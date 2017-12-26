package com.ahao.util.lang.time;

import com.ahao.util.lang.math.NumberHelper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 时间帮助类
 * Created by Ahaochan on 2017/8/21.
 */
public abstract class DateHelper {
    private static final Logger logger = LoggerFactory.getLogger(DateHelper.class);
    public static final String yyyyMMdd_hhmmssSSS = "yyyy-MM-dd hh:mm:ss:SSS";

    private DateHelper() {
        throw new AssertionError("工具类不允许实例化");
    }

    /**
     * 将 Date 格式化为 format格式 的日期
     *
     * @param date   时间
     * @param format 格式
     * @return 格式化后的日期
     */
    public static String getString(long date, String format) {
        return getString(new Date(date), format);
    }
    public static String getString(Date date, String format) {
        return DateFormatUtils.format(date, format);
    }
    public static String getString(String date, String fromFormat, String toFormat) {
        try {
            return getString(DateUtils.parseDate(date, fromFormat), toFormat);
        } catch (ParseException e) {
            logger.error(date+"解析为"+fromFormat+"格式失败:", e);
        }
        return null;
    }

    /**
     * 获取 format 格式化的当前时间
     *
     * @param format 日期格式
     * @return 当前时间
     */
    public static String getNow(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }
    public static int getNowYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    public static Date getNowYearStart(){
        return getYearStart(getNowYear());
    }
    public static Date getNowYearEnd(){
        return getYearEnd(getNowYear());
    }
    public static int getNowMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }
    public static int getNowDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 将 format形式的date字符串转化为 long型日期
     *
     * @param date   日期字符串
     * @param format 格式
     * @return 返回自1970年1月1日00:00:00 GMT以来的毫秒数
     */
    public static long getTime(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            logger.error("时间"+date+"解析异常:", e);
        }
        return -1L;
    }

    /**
     * 判断 date日期 是否在 start日期 和 end日期 之间
     *
     * @param date  判断日期
     * @param start 开始日期下限
     * @param end   结束日期上限
     * @return 判断 date日期 是否在 start日期 和 end日期 之间
     */
    public static boolean isBetween(Date date, Date start, Date end) {
        return isBetween(date.getTime(), start.getTime(), end.getTime());
    }
    public static boolean isBetween(long date, long start, long end) {
        return NumberHelper.isBetween(date, start, end);
    }

    /**
     * 获取开始日期和结束日期的间隔
     *
     * @param start    开始日期
     * @param end      结束日期
     * @param timeUnit 转化后的时间格式, 天数、秒数、毫秒数等
     * @return 计算两个日期的时间间隔
     */
    public static long getBetween(long start, long end, TimeUnit timeUnit) {
        return timeUnit.convert(end - start, TimeUnit.MILLISECONDS);
    }

    public static Date getYearStart(int year){
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.JANUARY, 1, 0, 0, 0);
        return cal.getTime();
    }
    public static Date getYearEnd(int year){
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return cal.getTime();
    }
    public static Date getMonthStart(int year, int month){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, 1, 0, 0, 0);
        return cal.getTime();
    }
    public static Date getMonthEnd(int year, int month){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, 1, 23, 59, 59);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
}
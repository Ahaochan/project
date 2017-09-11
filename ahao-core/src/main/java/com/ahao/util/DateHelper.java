package com.ahao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间帮助类
 * Created by Ahaochan on 2017/8/21.
 */
public abstract class DateHelper {
    private static final Logger logger = LoggerFactory.getLogger(DateHelper.class);
    private DateHelper(){

    }

    /**
     * 将 Date 格式化为 format格式 的日期
     *
     * @param date   时间
     * @param format 格式
     * @return 格式化后的日期
     */
    public static String getString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    /**
	 * 提取当前年份
	 * @return 当前年份
	 */
	public static int getCurrentYear()
	{
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		return year;
	}
	/**
	 * 提取当前月份
	 * @return 月份
	 */
	public static int getCurrentMonth()
	{
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		return month+1;
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
     * 获取 format 格式化的当前时间
     *
     * @param format 日期格式
     * @return 当前时间
     */
    public static String getNow(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
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

    /**
     * 判断 date日期 是否在 start日期 和 end日期 之间
     *
     * @param date  判断日期
     * @param start 开始日期下限
     * @param end   结束日期上限
     * @return 判断 date日期 是否在 start日期 和 end日期 之间
     */
    public static boolean isBetween(long date, long start, long end) {
        return NumberHelper.isBetween(date, start, end);
    }
}
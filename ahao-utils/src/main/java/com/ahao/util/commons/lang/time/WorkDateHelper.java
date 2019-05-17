//package com.ahao.util.commons.lang.time;
//
//import com.ahao.commons.config.Setter;
//import CollectionHelper;
//import org.apache.commons.collections4.CollectionUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
///**
// * 工作日判断的帮助类
// * 在 resource/config/application_setting.xml 中配置如下参数
// *  <holidays description="工作日的假日">
// *      <day name="1" value="2018-01-01" description="元旦"/>
// *      <day name="1" value="2018-02-15" description="春节"/>
// *      <day name="1" value="2018-02-16" description="春节"/>
// *      <day name="1" value="2018-02-17" description="春节"/>
// *      <day name="1" value="2018-02-18" description="春节"/>
// *      <day name="1" value="2018-02-19" description="春节"/>
// *      <day name="1" value="2018-02-20" description="春节"/>
// *      <day name="1" value="2018-02-21" description="春节"/>
// *      <day name="1" value="2018-04-05" description="清明"/>
// *      <day name="1" value="2018-04-06" description="清明"/>
// *      <day name="1" value="2018-04-07" description="清明"/>
// *      <day name="1" value="2018-04-29" description="五一"/>
// *      <day name="1" value="2018-04-30" description="五一"/>
// *      <day name="1" value="2018-05-01" description="五一"/>
// *      <day name="1" value="2018-06-16" description="端午"/>
// *      <day name="1" value="2018-06-17" description="端午"/>
// *      <day name="1" value="2018-06-18" description="端午"/>
// *      <day name="1" value="2018-09-22" description="中秋"/>
// *      <day name="1" value="2018-09-23" description="中秋"/>
// *      <day name="1" value="2018-09-24" description="中秋"/>
// *      <day name="1" value="2018-10-01" description="国庆"/>
// *      <day name="1" value="2018-10-02" description="国庆"/>
// *      <day name="1" value="2018-10-03" description="国庆"/>
// *      <day name="1" value="2018-10-04" description="国庆"/>
// *      <day name="1" value="2018-10-05" description="国庆"/>
// *      <day name="1" value="2018-10-06" description="国庆"/>
// *      <day name="1" value="2018-10-07" description="国庆"/>
// *  </holidays>
// *  <workingdays description="周未休息日的工作日">
// *      <day name="2" value="2018-04-08" description="调休成为工作日"/>
// *      <day name="2" value="2018-04-28" description="调休成为工作日"/>
// *      <day name="2" value="2018-09-29" description="调休成为工作日"/>
// *      <day name="2" value="2018-09-30" description="调休成为工作日"/>
// *  </workingdays>
// */
//public abstract class WorkDateHelper {
//    private static final Logger logger = LoggerFactory.getLogger(WorkDateHelper.class);
//
//
//    private WorkDateHelper() {
//        throw new AssertionError("工具类不允许实例化");
//    }
//
//
//    /**
//     * 计算工作日的时间差
//     * @param preDate 前一个日期
//     * @param nextDate 后一个日期
//     */
//    public static long dateDifference(Date preDate, Date nextDate){
//        return timeDifference(preDate.getTime(), nextDate.getTime());
//    }
//    public static long timeDifference(long preTime, long nextTime){
//        Calendar calendar = Calendar.getInstance();
//
//        // 1. 保证 前一个日期 大于 后一个日期
//        if(preTime > nextTime){
//            logger.error("前一个日期"+DateHelper.getString(preTime, DateHelper.yyyyMMdd_hhmmssSSS)+"不能大于后一个日期"+DateHelper.getString(nextTime, DateHelper.yyyyMMdd_hhmmssSSS));
//            return -1;
//        }
//
//        // 2. 校准 preTime, 调整到明天0点整点
//        long timeDiff = 0;
//        calendar.setTimeInMillis(preTime);
//        calendar.add(Calendar.DATE, 1);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//
//        // 2.1. 如果是节假日调休或工作日, 就记录校准时间差
//        if(isWorkTime(preTime)) {
//            timeDiff += calendar.getTimeInMillis() - preTime;
//        }
//        preTime = calendar.getTimeInMillis();
////        logger.debug("校准preTime:"+new Date(preTime)+","+DurationFormatUtils.formatDuration(timeDiff, "**dd HH:mm:ss**", true));
//
//        // 3. 校准 nextTime, 调整到当天0点整点
//        calendar.setTimeInMillis(nextTime);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//
//        // 3.1. 如果是节假日调休或工作日, 就记录校准时间差
//        if(isWorkTime(nextTime)) {
//            timeDiff += nextTime - calendar.getTimeInMillis();
//        }
//        nextTime = calendar.getTimeInMillis();
////        logger.debug("校准nextTime:"+new Date(nextTime)+","+DurationFormatUtils.formatDuration(timeDiff, "**dd HH:mm:ss**", true));
//
//        // 4. 模拟时间流逝, 计算在工作日内的时间差
//        while(preTime < nextTime) {
//            // 3. 如果是节假日调休或工作日, 就记录校准时间差
//            if(isWorkTime(preTime)) {
//                timeDiff += DateHelper.DAY_TIME;
//            }
//            preTime += DateHelper.DAY_TIME;
////            logger.debug("时间流逝:"+new Date(preTime)+","+DurationFormatUtils.formatDuration(timeDiff, "**dd HH:mm:ss**", true));
//        }
//
//        return timeDiff;
//    }
//
//    /**
//     * 判断是否为 工作日, 节假日调休或正常工作日
//     * @param time 计算的日期
//     */
//    public static boolean isWorkTime(long time){
//        String day = DateHelper.getString(time, DateHelper.yyyyMMdd);
//
//        // 1. 是节假日就返回false
//        List<String> holidays = Setter.getStringList("holidays.day");
//        if(CollectionUtils.isEmpty(holidays)){
//            logger.error("获取 holidays.day 值失败! 无法判断是否为工作日!");
//            return false;
//        }
//        if(CollectionHelper.contains(holidays, day)){
//            return false;
//        }
//
//        // 2. 是节假日调休就返回true
//        List<String> workingDays = Setter.getStringList("workingDays.day");
//        if(CollectionUtils.isEmpty(workingDays)){
//            logger.error("获取 workingDays.day 值失败! 无法判断是否为节假日调休!");
//            return false;
//        }
//        if(CollectionHelper.contains(workingDays, day)){
//            return true;
//        }
//
//        // 3. 是周末返回false
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(time);
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        if(dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY){
//            return false;
//        }
//
//        // 4. 是正常工作日就返回true
//        return true;
//    }
//    public static boolean isWorkDate(Date date){
//        return isWorkTime(date.getTime());
//    }
//}

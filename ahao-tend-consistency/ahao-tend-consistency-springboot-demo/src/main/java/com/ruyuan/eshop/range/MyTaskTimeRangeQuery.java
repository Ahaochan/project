package com.ruyuan.eshop.range;

import moe.ahao.tend.consistency.core.spi.query.TaskTimeRangeQuery;
import moe.ahao.tend.consistency.core.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * 自定义最终一致性任务时间范围查询定制
 *
 * @author zhonghuashishan
 **/
@Component // 需要加@Component 框架使用了spring容器来获取相关的实现类
public class MyTaskTimeRangeQuery implements TaskTimeRangeQuery {

    /**
     * 获取查询任务的初始时间 默认的开始时间为 查询12小时内的任务
     *
     * @return 启始时间
     */
    @Override
    public Date getStartTime() {
        return DateUtils.getDateByDayNum(new Date(), Calendar.HOUR, -12);
    }

    /**
     * 获取查询任务的结束时间
     *
     * @return 结束时间
     */
    @Override
    public Date getEndTime() {
        return new Date();
    }

    /**
     * 每次最多查询出多少个未完成的任务出来
     *
     * @return 未完成的任务数量
     */
    @Override
    public Long limitTaskCount() {
        return 200L;
    }
}

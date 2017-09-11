package com.ahao.invoice.unit.dao;

import com.ahao.entity.DataSet;
import com.ahao.invoice.unit.entity.UnitDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * Created by Ahaochan on 2017/8/13.
 */
@Repository
public interface UnitDAO extends Mapper<UnitDO> {
    /**
     * 分页查找
     *
     * @param start    从第start条数据开始
     * @param pageSize 查找pageSize条数据
     * @param sort     按sort字段排序
     * @param order    order为asc为正序, order为desc为逆序
     * @return 数据集合
     */
    Collection<UnitDO> selectPage(@Param("start") Integer start,
                                  @Param("pageSize") Integer pageSize,
                                  @Param("sort") String sort,
                                  @Param("order") String order);


    /**
     * 获取发票的购、销单位(分隔计算)的地理分布信息
     */
    List<DataSet> getDistribution();

    /**
     * 获取发票的购、销单位(合并计算)的地理分布信息
     */
    List<DataSet> getDistributionAll();
}

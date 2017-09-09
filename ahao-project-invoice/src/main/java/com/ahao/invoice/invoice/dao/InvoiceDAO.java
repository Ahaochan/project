package com.ahao.invoice.invoice.dao;

import com.ahao.entity.DataSet;
import com.ahao.invoice.invoice.entity.InvoiceDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * Created by Ahaochan on 2017/9/3.
 */
@Repository
public interface InvoiceDAO extends Mapper<InvoiceDO> {

    DataSet getForm(@Param("invoiceId") Long invoiceId);

    /**
     * 分页查找
     *
     * @param start    从第start条数据开始
     * @param pageSize 查找pageSize条数据
     * @param sort     按sort字段排序
     * @param order    order为asc为正序, order为desc为逆序
     * @return 数据集合
     */
    Collection<DataSet> selectPage(@Param("start") Integer start,
                                   @Param("pageSize") Integer pageSize,
                                   @Param("sort") String sort,
                                   @Param("order") String order);

    /**
     * 添加发票货物表的联系
     * @param invoiceId 发票Id
     * @param goodsId 货物Id
     * @param number 货物数量
     */
    void addRelate(@Param("invoiceId") Long invoiceId,
                   @Param("goodsId") Long goodsId,
                   @Param("number") Long number);


    /**
     * 获取按 yyyy-MM-dd 分布的进销项发票金额和税额
     */
    List<DataSet> getProfitGraph();

    /**
     * 获取 年份 月份 发票代码 某行政区划内的发票数量, 具体到市级
     * @param isSell 是否为销项发票
     */
    List<DataSet> getDistribution(@Param("isSell") int isSell);
}

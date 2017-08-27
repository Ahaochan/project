package com.ahao.invoice.product.goods.dao;

import com.ahao.entity.DataSet;
import com.ahao.invoice.product.goods.entity.GoodsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Collection;

/**
 * Created by Ahaochan on 2017/8/22.
 */
@Repository
public interface GoodsDAO extends Mapper<GoodsDO> {

    /**
     * 分页查找
     *
     * @param start    从第start条数据开始
     * @param pageSize 查找pageSize条数据
     * @param sort     按sort字段排序
     * @param order    order为asc为正序, order为desc为逆序
     * @return 数据集合
     */
    Collection<GoodsDO> selectPage(@Param("start") Integer start,
                                   @Param("pageSize") Integer pageSize,
                                   @Param("sort") String sort,
                                   @Param("order") String order);

    DataSet selectCategoryByKey(@Param("goodId") Long goodId);
}

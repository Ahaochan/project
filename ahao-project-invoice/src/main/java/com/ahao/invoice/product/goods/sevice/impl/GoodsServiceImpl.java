package com.ahao.invoice.product.goods.sevice.impl;

import com.ahao.entity.DataSet;
import com.ahao.invoice.product.goods.dao.GoodsDAO;
import com.ahao.invoice.product.goods.entity.GoodsDO;
import com.ahao.invoice.product.goods.sevice.GoodsService;
import com.ahao.service.impl.PageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.List;

/**
 * Created by Ahaochan on 2017/8/22.
 */
@Service
public class GoodsServiceImpl extends PageServiceImpl<GoodsDO> implements GoodsService {
    private static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);
    private GoodsDAO goodsDAO;

    @Autowired
    public GoodsServiceImpl(GoodsDAO goodsDAO) {
        this.goodsDAO = goodsDAO;
    }

    @Override
    protected Mapper<GoodsDO> dao() {
        return goodsDAO;
    }

    @Override
    protected Class<GoodsDO> clazz() {
        return GoodsDO.class;
    }

    @Override
    protected Collection<GoodsDO> getByPage(int start, int pageSize, String sort, String order) {
        return goodsDAO.selectPage(start, pageSize, sort, order);
    }

    @Override
    public DataSet selectCategoryByKey(Long goodId) {
        if(goodId == null){
            logger.error("货物id为空");
            return null;
        }
        return goodsDAO.selectCategoryByKey(goodId);
    }

    @Override
    public List<GoodsDO> selectByName(String name) {
        Example example = new Example(GoodsDO.class);
        example.selectProperties("id", "name", "specification", "unit", "unitePrice", "taxRate", "createTime")
                .createCriteria().andLike("name", name+"%");
        return goodsDAO.selectByExample(example);
    }
}

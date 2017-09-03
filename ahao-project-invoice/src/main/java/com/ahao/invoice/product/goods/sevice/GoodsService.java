package com.ahao.invoice.product.goods.sevice;

import com.ahao.entity.DataSet;
import com.ahao.invoice.product.goods.entity.GoodsDO;
import com.ahao.service.PageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ahaochan on 2017/8/22.
 */
@Service
public interface GoodsService extends PageService<GoodsDO> {
    DataSet selectCategoryByKey(Long goodId);

    List<GoodsDO> selectByName(String name);
}

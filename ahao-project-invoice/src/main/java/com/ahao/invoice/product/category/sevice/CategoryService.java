package com.ahao.invoice.product.category.sevice;

import com.ahao.invoice.product.category.entity.CategoryDO;
import com.ahao.service.PageService;

import java.util.List;

/**
 * Created by Ahaochan on 2017/8/21.
 */
public interface CategoryService extends PageService<CategoryDO> {
    List<CategoryDO> selectByName(String name);
}

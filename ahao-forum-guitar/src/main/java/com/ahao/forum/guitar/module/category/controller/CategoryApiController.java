package com.ahao.forum.guitar.module.category.controller;

import com.ahao.core.context.PageContext;
import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.CollectionHelper;
import com.ahao.core.util.web.PageIndicator;
import com.ahao.forum.guitar.module.category.service.CategoryService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryApiController {

    private CategoryService categoryService;
    @Autowired
    public CategoryApiController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/category-group-{page}")
    public AjaxDTO categoryGroup(@PathVariable Integer page,
                                  @RequestParam(required = false) String search){
        JSONObject result = new JSONObject();

        IDataSet userData = (IDataSet) SecurityUtils.getSubject().getPrincipal();
        long userId = userData.getLong("id");

        int pageSize = PageContext.getPageSize();
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = categoryService.getCategoryGroupByUserId(userId, search, "id", "name", "status");

        result.put("list", list);
        if(CollectionHelper.isEmpty(list)){
            return AjaxDTO.failure("获取数据为空");
        }

        PageInfo<IDataSet> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        String pageIndicator = PageIndicator.getBootstrap(total, page, pageSize);
        result.put("pageIndicator", pageIndicator);
        return AjaxDTO.success(result);
    }
}

package com.ahao.invoice.product.category.controller;

import com.ahao.invoice.product.category.entity.CategoryDO;
import com.ahao.invoice.product.category.sevice.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Ahaochan on 2017/8/19.
 */
@Controller
public class CategoryViewController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryViewController.class);
    private CategoryService categoryService;

    @Autowired
    public CategoryViewController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/product/categorys")
    public String all() {
        return "product/category/list";
    }

    @GetMapping("/product/category")
    public String add() {
        return "product/category/add";
    }

    @GetMapping("/product/category/{categoryId}")
    public ModelAndView modify(@PathVariable(value = "categoryId") Long categoryId) {
        ModelAndView mv = new ModelAndView("product/category/modify");
        CategoryDO category = categoryService.selectByKey(categoryId);
        mv.addObject(CategoryDO.TAG, category);
        mv.addObject("ce","ce1");
        return mv;
    }
}

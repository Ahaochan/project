package com.ahao.forum.guitar.module.category.controller;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.module.category.service.CategoryService;
import com.ahao.forum.guitar.module.post.service.PostService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);


    private CategoryService categoryService;
    private PostService postService;

    @Autowired
    public CategoryController(CategoryService categoryService, PostService postService) {
        this.categoryService = categoryService;
        this.postService = postService;
    }

    @GetMapping("/category-{categoryId}")
    public String category(@PathVariable Long categoryId,
                           @RequestParam(required = false) String search,
                           @RequestParam(defaultValue = "1") Integer page, Model model) {
        // 1. 获取板块信息
        IDataSet category = categoryService.getCategoryById(categoryId, "name", "status", "post_count");
        if (category == null) {
            logger.debug("板块信息获取错误, 重定向到index");
            return "redirect: index";
        }
        if (!category.getBoolean("status")) {
            logger.debug("板块被禁止访问, 重定向到index");
            return "redirect: index";
        }
        model.addAttribute("category", category);

        // 2. 获取该板块下的文章
        JSONObject posts = postService.getPosts(categoryId, search, page);
        model.addAttribute("posts", posts);

        return "category/category";
    }
}

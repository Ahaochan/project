package com.ahao.forum.guitar.manager.category.controller;

import com.ahao.core.context.PageContext;
import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.CollectionHelper;
import com.ahao.core.util.web.PageIndicator;
import com.ahao.forum.guitar.manager.category.service.CategoryService;
import com.ahao.forum.guitar.manager.forum.service.ForumService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private CategoryService categoryService;
    private ForumService forumService;

    @Autowired
    public CategoryController(CategoryService categoryService, ForumService forumService) {
        this.categoryService = categoryService;
        this.forumService = forumService;
    }

    @GetMapping("/category")
    public String category(Model model) {
        model.addAttribute("isExist", false);
        List<IDataSet> forums = forumService.getForums("id", "name", "status");
        model.addAttribute("forums", forums);
        return "manager/category/manager-category-detail";
    }

    @GetMapping("/category-{categoryId}")
    public String category(@PathVariable Long categoryId, Model model) {
        boolean isExist = false;
        if (categoryId != null && categoryId > 0) {
            IDataSet data = categoryService.getCategory(categoryId);
            if (data != null){
                isExist = true;
                model.addAttribute("category", data);
                List<IDataSet> forums = categoryService.getSelectedForums(categoryId);
                model.addAttribute("forums", forums);
            }
        }
        model.addAttribute("isExist", isExist);
        return "manager/category/manager-category-detail";
    }

    @PostMapping("/api/category/save")
    @ResponseBody
    public AjaxDTO save(@RequestParam(required = false) Long categoryId,
                        @RequestParam String name,
                        @RequestParam String description,
                        @RequestParam Integer status,
                        @RequestParam("forumIds[]") Long... forumIds) {
        // 1. 保存当前用户的实体联系, 返回 分区id
        long id = categoryService.saveCategory(categoryId, name, description, status, forumIds);
        return AjaxDTO.get(id>0);
    }


    @GetMapping("/categories")
    public String categoryList() {
        return "manager/category/manager-category-list";
    }

    @GetMapping("/api/categories/list-{page}")
    @ResponseBody
    public AjaxDTO categoryList(@PathVariable Integer page,
                                 @RequestParam(required = false) String search) {
        JSONObject result = new JSONObject();

        // 1. 获取已登录的用户数据
        IDataSet userData = (IDataSet) SecurityUtils.getSubject().getPrincipal();
        long userId = userData.getLong("id");

        // 2. 分页获取
        int pageSize = PageContext.getPageSize();
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = categoryService.getCategories(userId, search);
        result.put("list", list);
        if (CollectionHelper.isEmpty(list)) {
            return AjaxDTO.failure("获取数据为空");
        }

        // 3. 获取分页器
        PageInfo<IDataSet> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        String pageIndicator = PageIndicator.getBootstrap(total, page, pageSize);
        result.put("pageIndicator", pageIndicator);
        return AjaxDTO.success(result);
    }

    @PostMapping("/api/categories/delete")
    @ResponseBody
    public AjaxDTO delete(@RequestParam("categoryIds[]") Long... categoryIds) {
        int deleteCount = categoryService.deleteCategory(categoryIds);
        if (deleteCount > 0) {
            return AjaxDTO.success("删除成功, 删除" + deleteCount + "条记录");
        }
        return AjaxDTO.failure("删除失败, 请联系管理员");
    }

//    @GetMapping("/category-{categoryId}")
//    public String category(@PathVariable Long categoryId,
//                           @RequestParam(required = false) String search,
//                           @RequestParam(defaultValue = "1") Integer page, Model model) {
//        // 1. 获取板块信息
//        IDataSet category = categoryService.getCategoryById(categoryId, "name", "status", "post_count");
//        if (category == null) {
//            logger.debug("板块信息获取错误, 重定向到index");
//            return "redirect: index";
//        }
//        if (!category.getBoolean("status")) {
//            logger.debug("板块被禁止访问, 重定向到index");
//            return "redirect: index";
//        }
//        model.addAttribute("category", category);
//
//        // 2. 获取该板块下的文章
//        JSONObject posts = postService.getPosts(categoryId, search, page);
//        model.addAttribute("posts", posts);
//
//        return "category/category";
//    }


}

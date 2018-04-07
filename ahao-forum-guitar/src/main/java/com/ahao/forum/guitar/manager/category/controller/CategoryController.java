package com.ahao.forum.guitar.manager.category.controller;

import com.ahao.core.context.PageContext;
import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.CollectionHelper;
import com.ahao.core.util.web.PageIndicator;
import com.ahao.forum.guitar.manager.category.service.CategoryService;
import com.ahao.forum.guitar.manager.forum.service.ForumService;
import com.ahao.forum.guitar.manager.rbac.shiro.util.ShiroHelper;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    @Autowired
    public CategoryController(CategoryService categoryService, ForumService forumService) {
        this.categoryService = categoryService;
    }

    // TODO @RequiresPermissions("auth.category.add")
    @GetMapping("/category")
    public String category(Model model) {
        // 1. 新建分区, 所以传入 false, 表示不存在, 用于隐藏部分html代码
        model.addAttribute("isExist", false);
        List<IDataSet> forums = categoryService.getSelectedForums( -1L);
        model.addAttribute("forums", forums);
        return "manager/category/manager-category-detail";
    }

    // TODO @RequiresPermissions("auth.category.edit")
    @GetMapping("/category-{categoryId}")
    public String category(@PathVariable Long categoryId, Model model) {
        // 1. 编辑分区, 根据传入的分区id, 判断分区是否存在
        boolean isExist = false;
        if (categoryId != null && categoryId > 0) {
            IDataSet data = categoryService.getCategory(categoryId);
            if (data != null){
                isExist = true;
                // 2. 编辑分区, 如果存在, 则传入该分区的数据
                model.addAttribute("category", data);
                // 3. 判断是否拥有关联板块权限
                if(ShiroHelper.hasAllAuths("auth.category.relate.forums")) {
                    List<IDataSet> forums = categoryService.getSelectedForums(categoryId);
                    model.addAttribute("forums", forums);
                }
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

    // TODO @RequiresPermissions("auth.category.list")
    @GetMapping("/categories")
    public String categoryList() {
        return "manager/category/manager-category-list";
    }

    // TODO @RequiresPermissions("auth.category.list")
    @GetMapping("/api/categories/list-{page}")
    @ResponseBody
    public AjaxDTO categoryList(@PathVariable Integer page,
                                 @RequestParam(required = false) String search) {
        JSONObject result = new JSONObject();

        // 1. 获取已登录的用户数据
        long userId = ShiroHelper.getMyUserId();

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

    // TODO @RequiresPermissions("auth.category.delete")
    @PostMapping("/api/categories/delete")
    @ResponseBody
    public AjaxDTO delete(@RequestParam("categoryIds[]") Long... categoryIds) {
        int deleteCount = categoryService.deleteCategory(categoryIds);
        if (deleteCount > 0) {
            return AjaxDTO.success("删除成功, 删除" + deleteCount + "条记录");
        }
        return AjaxDTO.failure("删除失败, 请联系管理员");
    }
}

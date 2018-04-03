package com.ahao.forum.guitar.manager.forum.controller;

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
public class ForumController {
    private static final Logger logger = LoggerFactory.getLogger(ForumController.class);

    private CategoryService categoryService;
    private ForumService forumService;

    @Autowired
    public ForumController(CategoryService categoryService, ForumService forumService) {
        this.categoryService = categoryService;
        this.forumService = forumService;
    }

    @GetMapping("/forum")
    public String category(Model model) {
        model.addAttribute("isExist", false);
//        List<IDataSet> forums = categoryService.getCategory("id", "name", "status");
//        model.addAttribute("forums", forums);
        return "forum/manager-forum-detail";
    }

    @GetMapping("/forum-{forumId}")
    public String category(@PathVariable Long forumId, Model model) {
        boolean isExist = false;
        if (forumId != null && forumId > 0) {
            IDataSet data = categoryService.getCategory(forumId);
            if (data != null){
                isExist = true;
                model.addAttribute("category", data);
                List<IDataSet> forums = categoryService.getSelectedForums(forumId);
                model.addAttribute("forums", forums);
            }
        }
        model.addAttribute("isExist", isExist);
        return "forum/manager-forum-detail";
    }

    @PostMapping("/api/forum/save")
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


    @GetMapping("/forums")
    public String forumList() {
        return "forum/manager-forum-list";
    }

    @GetMapping("/api/forums/list-{page}")
    @ResponseBody
    public AjaxDTO forumList(@PathVariable Integer page,
                                 @RequestParam(required = false) String search) {
        JSONObject result = new JSONObject();

        // 1. 获取已登录的用户数据
        IDataSet userData = (IDataSet) SecurityUtils.getSubject().getPrincipal();
        long userId = userData.getLong("id");

        // 2. 分页获取
        int pageSize = PageContext.getPageSize();
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = forumService.getForumsTable(userId, search);
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

    @PostMapping("/api/forums/delete")
    @ResponseBody
    public AjaxDTO delete(@RequestParam("forumIds[]") Long... forumIds) {
        int deleteCount = forumService.deleteForum(forumIds);
        if (deleteCount > 0) {
            return AjaxDTO.success("删除成功, 删除" + deleteCount + "条记录");
        }
        return AjaxDTO.failure("删除失败, 请联系管理员");
    }
}

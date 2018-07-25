package com.ahao.forum.guitar.module.forum.controller;

import com.ahao.commons.entity.AjaxDTO;
import com.ahao.commons.entity.IDataSet;
import com.ahao.commons.html.page.PaginationBootstrap;
import com.ahao.commons.spring.context.PageContext;
import com.ahao.commons.util.lang.CollectionHelper;
import com.ahao.forum.guitar.module.forum.service.ForumService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller("forumForeController")
public class ForumController {

    private ForumService forumService;
    public ForumController(ForumService forumService){
        this.forumService = forumService;
    }

    @GetMapping("/forum-{forumId}")
    public String forum(@PathVariable Long forumId, Model model) {
        IDataSet forum = forumService.getForum(forumId);
        model.addAttribute("forum", forum);

        return "forum/forum";
    }

    @GetMapping("/api/forum-{forumId}/thread/list-{page}")
    @ResponseBody
    public AjaxDTO threadList(@PathVariable Long forumId,
                              @PathVariable Integer page,
                              @RequestParam(defaultValue = "") String search){
        JSONObject result = new JSONObject();

        // 1. 分页获取
        int pageSize = PageContext.getPageSize();
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = forumService.getThread(forumId, search);
        result.put("list", list);
        if (CollectionHelper.isEmpty(list)) {
            return AjaxDTO.failure("获取数据为空");
        }

        // 2. 获取分页器
        PageInfo<IDataSet> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        String pageIndicator = PaginationBootstrap.getBootstrap(total, page, pageSize);
        result.put("pageIndicator", pageIndicator);
        return AjaxDTO.success(result);

    }
}

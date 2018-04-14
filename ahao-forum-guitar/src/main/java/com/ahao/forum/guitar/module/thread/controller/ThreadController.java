package com.ahao.forum.guitar.module.thread.controller;

import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.DataSet;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.CollectionHelper;
import com.ahao.core.util.web.PageIndicator;
import com.ahao.forum.guitar.manager.rbac.shiro.util.ShiroHelper;
import com.ahao.forum.guitar.module.thread.service.ThreadService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ThreadController {
    private ThreadService threadService;
    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @GetMapping("/forum-{forumId}/new")
    public String newThread(@PathVariable Long forumId, Model model){
        if(forumId == null || forumId <= 0){
            return "redirect: /";
        }
        model.addAttribute("isExist", false);
        IDataSet data = new DataSet();
        data.put("forum_id", forumId);
        model.addAttribute("thread", data);
        return "thread/thread-detail";
    }

    @GetMapping("/thread-{threadId}/modify")
    public String modifyThread(@PathVariable Long threadId, Model model){
        boolean isExist = false;
        if (threadId != null && threadId > 0) {
            IDataSet thread = threadService.getThread(threadId);
            if (thread != null){
                isExist = true;
                model.addAttribute("thread", thread);
            }
        }
        model.addAttribute("isExist", isExist);
        return "thread/thread-detail";
    }

    @GetMapping("/thread-{threadId}")
    public String threadAndPosts(@PathVariable Long threadId, Model model){
        IDataSet thread = threadService.getThread(threadId);
        model.addAttribute("thread", thread);
        return "thread/thread-posts";
    }

    @PostMapping("/api/thread-{threadId}/save")
    @ResponseBody
    public AjaxDTO save(@PathVariable Long threadId, @RequestParam Long forumId,
                        @RequestParam String title, @RequestParam String content) {
        // 1. 保存当前用户的实体联系, 返回 分区id
        long userId = ShiroHelper.getMyUserId();
        Long id = threadService.saveThread(threadId, title, content, userId, forumId);
        return AjaxDTO.get(id>0, "", id);
    }

    @GetMapping("/api/thread-{threadId}/posts")
    @ResponseBody
    public AjaxDTO posts(@PathVariable Long threadId, @RequestParam Integer page){
        JSONObject result = new JSONObject();

        // 1. 分页获取
        int pageSize = 10;
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = threadService.getPosts(threadId);
        result.put("list", list);
        if (CollectionHelper.isEmpty(list)) {
            return AjaxDTO.failure("获取数据为空");
        }

        // 2. 获取分页器
        PageInfo<IDataSet> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        String pageIndicator = PageIndicator.getBootstrap(total, page, pageSize);
        result.put("pageIndicator", pageIndicator);
        return AjaxDTO.success(result);
    }
}

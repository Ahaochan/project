package com.ahao.forum.guitar.module.thread.controller;

import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.CollectionHelper;
import com.ahao.core.util.web.PageIndicator;
import com.ahao.forum.guitar.module.thread.service.ThreadService;
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

@Controller
public class ThreadController {
    private ThreadService threadService;
    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @GetMapping("/thread-{threadId}")
    public String thread(@PathVariable Long threadId, Model model){
        IDataSet thread = threadService.getThread(threadId);
        model.addAttribute("thread", thread);
        return "thread/thread";
    }

    @GetMapping("/api/thread-{threadId}/posts")
    @ResponseBody
    public AjaxDTO posts(@PathVariable Long threadId,
                         @RequestParam Integer page){
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

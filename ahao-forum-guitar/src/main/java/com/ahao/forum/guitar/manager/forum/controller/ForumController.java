package com.ahao.forum.guitar.manager.forum.controller;

import com.ahao.commons.entity.AjaxDTO;
import com.ahao.commons.entity.IDataSet;
import com.ahao.commons.html.page.PaginationBootstrap;
import com.ahao.commons.spring.context.PageContext;
import com.ahao.commons.util.lang.CollectionHelper;
import com.ahao.forum.guitar.manager.forum.service.ForumService;
import com.ahao.forum.guitar.manager.rbac.shiro.util.ShiroHelper;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("forumBackController")
@RequestMapping("/manager")
public class ForumController {
    private static final Logger logger = LoggerFactory.getLogger(ForumController.class);

    private ForumService forumService;
    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping("/forum")
    public String forum(Model model) {
        model.addAttribute("isExist", false);
        return "manager/forum/manager-forum-detail";
    }

    @GetMapping("/forum-{forumId}")
    public String forum(@PathVariable Long forumId, Model model) {
        boolean isExist = false;
        if (forumId != null && forumId > 0) {
            IDataSet data = forumService.getForum(forumId);
            if (data != null){
                isExist = true;
                model.addAttribute("forum", data);
                // TODO 加载主题和回复
            }
        }
        model.addAttribute("isExist", isExist);
        return "manager/forum/manager-forum-detail";
    }

    @PostMapping("/api/forum/save")
    @ResponseBody
    public AjaxDTO save(@RequestParam(required = false) Long forumId,
                        @RequestParam String name,
                        @RequestParam String description,
                        @RequestParam(defaultValue = "1") Integer status,
                        @RequestParam(defaultValue = "") String iconUrl) {
        long id = forumService.saveForum(forumId, name, description, status, iconUrl);
        return AjaxDTO.get(id>0);
    }


    @GetMapping("/forums")
    public String forumList() {
        return "manager/forum/manager-forum-list";
    }

    @GetMapping("/api/forums/list-{page}")
    @ResponseBody
    public AjaxDTO forumList(@PathVariable Integer page,
                                @RequestParam(required = false) String search) {
        JSONObject result = new JSONObject();
        long userId = ShiroHelper.getMyUserId();

        // 1. 分页获取
        int pageSize = PageContext.getPageSize();
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = forumService.getForums(userId, search);
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

//    @PostMapping("/api/forums/delete")
//    @ResponseBody
//    public AjaxDTO delete(@RequestParam("forumIds[]") Long... forumIds) {
//        boolean success = forumService.deleteForum(forumIds);
//        if (success) {
//            return AjaxDTO.success("删除成功, 删除" + forumIds.length + "条记录");
//        }
//        return AjaxDTO.failure("删除失败, 请联系管理员");
//    }
}

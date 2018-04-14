package com.ahao.forum.guitar.module.post.controller;

import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.manager.rbac.shiro.util.ShiroHelper;
import com.ahao.forum.guitar.module.post.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    private PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/thread-{threadId}/new-post")
    public String newPost(@PathVariable Long threadId, @RequestParam(defaultValue = "-1") Long prePostId,
                          Model model){
        if (threadId != null && threadId > 0) {
            IDataSet thread = postService.getThreadById(threadId);
            model.addAttribute("thread", thread);
            IDataSet prePost = postService.getPost(prePostId);
            model.addAttribute("prePost", prePost);
        }
        model.addAttribute("isExist", false);
        return "post/post-detail";
    }

    @GetMapping("/post-{postId}/modify")
    public String modifyPost(@PathVariable Long postId, Model model){
        boolean isExists = false;
        if (postId != null && postId > 0) {
            // 1. 获取主题信息
            IDataSet thread = postService.getThreadByPostId(postId);
            model.addAttribute("thread", thread);
            // 2. 获取回复信息
            IDataSet post = postService.getPost(postId);
            model.addAttribute("post", post);
            isExists = true;
        }
        model.addAttribute("isExist", isExists);
        return "post/post-detail";
    }


    @PostMapping("/api/post/save")
    @ResponseBody
    public AjaxDTO save(@RequestParam(defaultValue = "-1") Long postId,
                        @RequestParam(defaultValue = "-1") Long prePostId, @RequestParam Long threadId,
                        @RequestParam String content) {
        // 1. 保存当前用户的实体联系, 返回 分区id
        long userId = ShiroHelper.getMyUserId();
        Long id = postService.savePost(postId, prePostId, content, userId, threadId);
        return AjaxDTO.get(id>0, "", id);
    }

    @PostMapping("/manager/api/post/delete")
    @ResponseBody
    public AjaxDTO delete(@RequestParam("postIds[]") Long... postIds) {
        int deleteCount = postService.deletePost(postIds);
        if (deleteCount > 0) {
            return AjaxDTO.success("删除成功, 删除" + deleteCount + "条记录");
        }
        return AjaxDTO.failure("删除失败, 请联系管理员");
    }
}

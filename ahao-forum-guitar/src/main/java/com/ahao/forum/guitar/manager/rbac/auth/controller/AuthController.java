package com.ahao.forum.guitar.manager.rbac.auth.controller;

import com.ahao.core.context.PageContext;
import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.CollectionHelper;
import com.ahao.core.util.web.PageIndicator;
import com.ahao.forum.guitar.manager.rbac.auth.service.AuthService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping("/auth")
    public String auth(Model model) {
        model.addAttribute("isExist", false);
        return "manager/auth/manager-auth-detail";
    }

    @GetMapping("/auth-{authId}")
    public String auth(@PathVariable Long authId, Model model) {
        boolean isExist = false;
        if (authId != null && authId > 0) {
            IDataSet data = authService.getAuth(authId);
            if (data != null){
                isExist = true;
                model.addAttribute("auth", data);
            }
        }
        model.addAttribute("isExist", isExist);
        return "manager/auth/manager-auth-detail";
    }

    @PostMapping("/api/auth/save")
    @ResponseBody
    public AjaxDTO save(@RequestParam(required = false) Long authId,
                        @RequestParam String name,
                        @RequestParam String description,
                        @RequestParam Integer enabled) {
        // 1. 保存当前用户的实体联系, 返回 分区id
        long id = authService.saveAuth(authId, name, description, enabled);
        return AjaxDTO.get(id>0);
    }


    @GetMapping("/auths")
    public String authList() {
        return "manager/auth/manager-auth-list";
    }

    @GetMapping("/api/auths/list-{page}")
    @ResponseBody
    public AjaxDTO authList(@PathVariable Integer page,
                                @RequestParam(required = false) String search) {
        JSONObject result = new JSONObject();

        // 1. 分页获取
        int pageSize = PageContext.getPageSize();
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = authService.getAuths(search);
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

    @PostMapping("/api/auths/delete")
    @ResponseBody
    public AjaxDTO delete(@RequestParam("authIds[]") Long... authIds) {
        int deleteCount = authService.deleteAuth(authIds);
        if (deleteCount > 0) {
            return AjaxDTO.success("删除成功, 删除" + deleteCount + "条记录");
        }
        return AjaxDTO.failure("删除失败, 请联系管理员");
    }

}

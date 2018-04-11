package com.ahao.forum.guitar.manager.rbac.role.controller;

import com.ahao.core.context.PageContext;
import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.CollectionHelper;
import com.ahao.core.util.web.PageIndicator;
import com.ahao.forum.guitar.manager.rbac.role.service.RoleService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manager")
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    private RoleService roleService;
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @GetMapping("/role")
    public String role(Model model) {
        model.addAttribute("isExist", false);
        List<IDataSet> auths = roleService.getSelectedAuths(-1L);
        model.addAttribute("auths", auths);
        return "manager/role/manager-role-detail";
    }

    @GetMapping("/role-{roleId}")
    public String role(@PathVariable Long roleId, Model model) {
        boolean isExist = false;
        if (roleId != null && roleId > 0) {
            IDataSet data = roleService.getRole(roleId);
            if (data != null){
                isExist = true;
                model.addAttribute("role", data);
                List<IDataSet> auths = roleService.getSelectedAuths(roleId);
                model.addAttribute("auths", auths);
            }
        }
        model.addAttribute("isExist", isExist);
        return "manager/role/manager-role-detail";
    }

    @PostMapping("/api/role/save")
    @ResponseBody
    public AjaxDTO save(@RequestParam(required = false) Long roleId,
                        @RequestParam String name,
                        @RequestParam String description,
                        @RequestParam(defaultValue = "0") Integer weight,
                        @RequestParam(defaultValue = "1") Integer enabled,
                        @RequestParam("authIds[]") Long... authIds) {
        // 1. 保存当前用户的实体联系, 返回 分区id
        long id = roleService.saveRole(roleId, name, description, weight, enabled, authIds);
        return AjaxDTO.get(id>0);
    }


    @GetMapping("/roles")
    public String roleList() {
        return "manager/role/manager-role-list";
    }

    @GetMapping("/api/roles/list-{page}")
    @ResponseBody
    public AjaxDTO roleList(@PathVariable Integer page,
                            @RequestParam(required = false) String search) {
        JSONObject result = new JSONObject();

        // 1. 获取已登录的用户数据
        IDataSet userData = (IDataSet) SecurityUtils.getSubject().getPrincipal();

        // 2. 分页获取
        int pageSize = PageContext.getPageSize();
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = roleService.getRoles(search);
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

    @PostMapping("/api/roles/delete")
    @ResponseBody
    public AjaxDTO delete(@RequestParam("roleIds[]") Long... roleIds) {
        boolean success = roleService.deleteRole(roleIds);
        if (success) {
            return AjaxDTO.success("删除成功, 删除" + roleIds.length + "条记录");
        }
        return AjaxDTO.failure("删除失败, 请联系管理员");
    }
}

package com.ahao.invoice.admin.role.controller;

import com.ahao.invoice.admin.auth.service.AuthService;
import com.ahao.invoice.admin.role.entity.RoleDO;
import com.ahao.invoice.admin.role.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Ahaochan on 2017/8/11.
 */
@Controller
public class RoleViewController {
    private static final Logger logger = LoggerFactory.getLogger(com.ahao.invoice.admin.user.controller.UserViewController.class);
    private RoleService roleService;

    @Autowired
    public RoleViewController(RoleService roleService, AuthService authService) {
        this.roleService = roleService;
    }

    @GetMapping("/admin/roles")
    public String all() {
        return "admin/role/list";
    }

    @GetMapping("/admin/role")
    public String add() {
        return "admin/role/add";
    }

    @GetMapping("/admin/role/{roleId}")
    public ModelAndView modify(@PathVariable(value = "roleId") Long roleId) {
        ModelAndView mv = new ModelAndView("admin/role/modify");
        RoleDO role = roleService.selectByKey(roleId);
        mv.addObject(RoleDO.TAG, role);
        return mv;
    }
}

package com.ahao.invoice.admin.user.controller;

import com.ahao.invoice.admin.role.service.RoleService;
import com.ahao.invoice.admin.user.entity.UserDO;
import com.ahao.invoice.admin.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Avalon on 2017/5/12.
 */
@Controller
public class UserViewController {
    private static final Logger logger = LoggerFactory.getLogger(UserViewController.class);
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserViewController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/admin/users")
    public String all() {
        return "admin/user/list";
    }

    @GetMapping("/admin/user")
    public String add() {
        return "admin/user/add";
    }

    @GetMapping("/admin/user/{userId}")
    public ModelAndView modify(@PathVariable(value = "userId") Long userId) {
        ModelAndView mv = new ModelAndView("admin/user/modify");
        UserDO user = userService.selectByKey(userId);
        mv.addObject(UserDO.TAG, user);
        return mv;
    }
}

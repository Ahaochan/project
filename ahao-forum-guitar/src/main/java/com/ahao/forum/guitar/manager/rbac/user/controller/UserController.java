package com.ahao.forum.guitar.manager.rbac.user.controller;

import com.ahao.forum.guitar.manager.rbac.auth.service.AuthService;
import com.ahao.forum.guitar.manager.rbac.role.service.RoleService;
import com.ahao.forum.guitar.manager.rbac.user.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;
    private RoleService roleService;
    private AuthService authService;

    public UserController(UserService userService, RoleService roleService, AuthService authService) {
        this.userService = userService;
        this.roleService = roleService;
        this.authService = authService;
    }

    @RequiresAuthentication
    @GetMapping("/password")
    public String modifyPassword() {
        return "admin/pane/pane-password";
    }


    @GetMapping("/admin/user")
    public String list() {
        return "admin/pane/pane-user";
    }

    @GetMapping("/admin/user-{userId}")
    public void userInfo(@PathVariable("userId") Integer userId) {

    }
}

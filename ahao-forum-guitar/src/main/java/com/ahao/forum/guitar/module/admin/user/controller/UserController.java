package com.ahao.forum.guitar.module.admin.user.controller;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.module.admin.auth.service.AuthService;
import com.ahao.forum.guitar.module.admin.role.service.RoleService;
import com.ahao.forum.guitar.module.admin.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;
    private RoleService roleService;
    private AuthService authService;

    public UserController(UserService userService, RoleService roleService, AuthService authService){
        this.userService = userService;
        this.roleService = roleService;
        this.authService = authService;
    }

    @GetMapping("/profile")
    public String profile(Model model){
        // 1. 获取用户信息
        IDataSet currentUser = (IDataSet) SecurityUtils.getSubject().getPrincipal();
        long userId = currentUser.getLong("id");
        model.addAttribute("user", currentUser);

        // 2. 获取用户资料
        IDataSet profile = userService.getProfile(userId, "email", "sex", "qq", "city");
        model.addAttribute("profile", profile);

        // 3. 获取角色资料
        List<IDataSet> roles = roleService.getSelectedRole(userId, "id", "description");
        model.addAttribute("roles", roles);

        // 3. 获取权限资料
        List<IDataSet> auths = authService.getSelectedAuth(userId, "id", "description");
        model.addAttribute("auths", auths);

        return "admin/pane/pane-profile";
    }


    @GetMapping("/admin/user")
    public String list(){
        return "admin/pane/pane-user";
    }

    @GetMapping("/admin/user-{userId}")
    public void userInfo(@PathVariable("userId") Integer userId){

    }
}

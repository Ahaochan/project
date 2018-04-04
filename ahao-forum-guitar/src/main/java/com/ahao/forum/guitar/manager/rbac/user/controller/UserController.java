package com.ahao.forum.guitar.manager.rbac.user.controller;

import com.ahao.core.context.PageContext;
import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.CollectionHelper;
import com.ahao.core.util.web.PageIndicator;
import com.ahao.forum.guitar.manager.rbac.auth.service.AuthService;
import com.ahao.forum.guitar.manager.rbac.role.service.RoleService;
import com.ahao.forum.guitar.manager.rbac.user.service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @GetMapping("/user")
//    public String user() {
//        return "admin/pane/pane-user";
//    }

//    @GetMapping("/user-{userId}")
//    public void user(@PathVariable("userId") Integer userId) {
//    }

//    @PostMapping("/api/user/save")
//    @ResponseBody
//    public AjaxDTO save(@RequestParam(required = false) Long categoryId,
//                        @RequestParam String name,
//                        @RequestParam String description,
//                        @RequestParam Integer status,
//                        @RequestParam("forumIds[]") Long... forumIds) {
//        // 1. 保存当前用户的实体联系, 返回 分区id
//        long id = categoryService.saveCategory(categoryId, name, description, status, forumIds);
//        return AjaxDTO.get(id>0);
//    }

    @GetMapping("/users")
    public String list() {
        return "manager/user/manager-user-list";
    }

    @GetMapping("/api/users/list-{page}")
    @ResponseBody
    public AjaxDTO userList(@PathVariable Integer page,
                                @RequestParam(required = false) String search) {
        JSONObject result = new JSONObject();

        // 1. 获取已登录的用户数据
        IDataSet userData = (IDataSet) SecurityUtils.getSubject().getPrincipal();
        long userId = userData.getLong("id");

        // 2. 分页获取
        int pageSize = PageContext.getPageSize();
        int weight = userService.getMaxRoleWeight(userId);
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = userService.getUsersTable(weight, search);
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

    @PostMapping("/api/users/delete")
    @ResponseBody
    public AjaxDTO delete(@RequestParam("userIds[]") Long... userIds) {
        int deleteCount = userService.deleteUser(userIds);
        if (deleteCount > 0) {
            return AjaxDTO.success("删除成功, 删除" + deleteCount + "条记录");
        }
        return AjaxDTO.failure("删除失败, 请联系管理员");
    }
}

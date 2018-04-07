package com.ahao.forum.guitar.manager.rbac.user.controller;

import com.ahao.core.config.SystemConfig;
import com.ahao.core.context.PageContext;
import com.ahao.core.entity.AjaxDTO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.CollectionHelper;
import com.ahao.core.util.web.PageIndicator;
import com.ahao.forum.guitar.manager.rbac.auth.service.AuthService;
import com.ahao.forum.guitar.manager.rbac.role.service.RoleService;
import com.ahao.forum.guitar.manager.rbac.shiro.util.ShiroHelper;
import com.ahao.forum.guitar.manager.rbac.user.service.UserService;
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

    @GetMapping("/user")
    public String user(Model model) {
        model.addAttribute("isExist", false);
        // 1. 获取所有角色信息 供选择
        List<IDataSet> roles = userService.getRoles();
        model.addAttribute("roles", roles);
        // 2. 获取所有分区信息 供选择
        List<IDataSet> categories = userService.getCategories();
        model.addAttribute("categories", categories);
        // 3. 获取所有板块信息 供选择
        List<IDataSet> forums = userService.getForums();
        model.addAttribute("forums", forums);
        return "manager/user/manager-user-detail";
    }

    @GetMapping("/user-{userId}")
    public String user(@PathVariable("userId") Long userId, Model model) {
        // 1. 判断用户是否存在
        if (userId == null || userId <= 0) {
            model.addAttribute("isExist", false);
            return "manager/user/manager-user-detail";
        }
        IDataSet data = userService.getUser(userId);
        if (data == null){
            model.addAttribute("isExist", false);
            return "manager/user/manager-user-detail";
        }

        // 2. 判断已登录用户是否有权限操作
        if(ShiroHelper.getMyUserWeight() <= data.getInt("weight")) {
            model.addAttribute("errorMsg", "没有权限对该用户进行操作");
            // TODO 返回没有权限的页面
//            return "123456789";
        }

        model.addAttribute("user", data);
        // 3. 获取限制在当前用户权限值之下的角色, 用 selected 标记是否已选择
        List<IDataSet> roles = userService.getSelectedRoles(userId);
        model.addAttribute("roles", roles);

        // 4. 检测是否包含分区, 板块角色
        final int superModeratorId = SystemConfig.instance().getInt("role.super-moderator", "id");
        final int moderatorId = SystemConfig.instance().getInt("role.moderator", "id");
        model.addAttribute("superModeratorId", superModeratorId);
        model.addAttribute("moderatorId", moderatorId);
        for (IDataSet role : roles) {
            boolean isSelected = role.getBoolean("selected");
            // 4.1. 未选择则跳过
            if(!isSelected){
                continue;
            }

            // 4.2. 选择的话, 判断是否为分区版主或版主角色, 加入 model, 用于显示对应的列表
            int roleId = role.getInt("id");
            if(roleId == superModeratorId){
                model.addAttribute("showCategory", true);
                break;
            } else if(roleId == moderatorId){
                model.addAttribute("showForum", true);
                break;
            }
        }

        // 4. 获取所有已选择的分区信息 供选择
        List<IDataSet> categories = userService.getSelectedCategories(userId);
        model.addAttribute("categories", categories);
        // 5. 获取所有已选择的板块信息 供选择
        List<IDataSet> forums = userService.getSelectedForums(userId);
        model.addAttribute("forums", forums);

        model.addAttribute("isExist", true);
        return "manager/user/manager-user-detail";
    }

    @PostMapping("/api/user/save")
    @ResponseBody
    public AjaxDTO save(@RequestParam(required = false) Long userId,
                        @RequestParam String username,
                        @RequestParam String password,
                        @RequestParam(defaultValue = "") String email,
                        @RequestParam(defaultValue = "0") Integer sex,
                        @RequestParam(defaultValue = "") String qq,
                        @RequestParam(defaultValue = "") String city,
                        @RequestParam(defaultValue = "1") Integer enabled,
                        @RequestParam(value = "roleId", required = false) Long roleId,
                        @RequestParam(value = "categoryIds[]", required = false) Long[] categoryIds,
                        @RequestParam(value = "forumIds[]", required = false) Long[] forumIds) {
        // 1. 保存当前用户的实体联系, 返回 分区id
        long id = userService.saveUser(userId, username, password,
                email, sex, qq, city, enabled, roleId, categoryIds, forumIds);
        return AjaxDTO.get(id>0);
    }

    @GetMapping("/users")
    public String list() {
        return "manager/user/manager-user-list";
    }

    @GetMapping("/api/users/list-{page}")
    @ResponseBody
    public AjaxDTO userList(@PathVariable Integer page,
                                @RequestParam(required = false) String search) {
        JSONObject result = new JSONObject();

        // 1. 分页获取
        int pageSize = PageContext.getPageSize();
        int weight = ShiroHelper.getMyUserWeight();
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = userService.getUsersTable(weight, search);
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

package com.ahao.invoice.admin.user.controller;

import com.ahao.config.SpringConfig;
import com.ahao.context.PageContext;
import com.ahao.entity.AjaxDTO;
import com.ahao.entity.DropDownListDTO;
import com.ahao.entity.PageDTO;
import com.ahao.entity.TableDTO;
import com.ahao.invoice.admin.role.entity.RoleDO;
import com.ahao.invoice.admin.role.service.RoleService;
import com.ahao.invoice.admin.user.entity.UserDO;
import com.ahao.invoice.admin.user.service.UserService;
import com.ahao.util.PageUrlBuilder;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by Avalon on 2017/5/12.
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    //----------------------POST------------------//
    @PostMapping("/admin/user/{userId}")
    @Transactional
    public ModelAndView modify(HttpServletRequest request,
                               @RequestParam(name = "role", defaultValue = "") String[] roleIds,
                               @Valid UserDO validUser, BindingResult result) {
        ModelAndView mv = new ModelAndView("admin/user/user_modify");
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                logger.debug("验证错误: " + e.getDefaultMessage());
            }
            mv.addObject("tip", AjaxDTO.failure(
                    SpringConfig.getString("modify.failure", request)));
            Map<RoleDO, Boolean> roles = roleService.getSelectedRole(roleIds);
            mv.addObject("role", roles);
        } else {
            userService.update(validUser);
            roleService.addRelate(validUser.getId(), roleIds);

            mv.addObject("tip", AjaxDTO.success(
                    SpringConfig.getString("modify.success", request)));
            Map<RoleDO, Boolean> roles = roleService.getSelectedRole(validUser.getId());
            mv.addObject("role", roles);
        }

        return mv;
    }

    @PostMapping("/admin/user")
    @Transactional
    public String add(HttpServletRequest request,
                      @RequestParam(name = "role", defaultValue = "") String[] roleIds,
                      @Valid UserDO validUser, BindingResult result,
                      Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                logger.debug("验证错误: " + e.getDefaultMessage());
            }
            model.addAttribute("tip", AjaxDTO.failure(
                    SpringConfig.getString("insert.failure", new Object[]{validUser.getId()}, request)));
            Map<RoleDO, Boolean> roles = roleService.getSelectedRole(roleIds);
            model.addAttribute("role", roles);
            return "admin/user/user_add";
        } else {
            userService.insert(validUser);
            roleService.addRelate(validUser.getId(), roleIds);
            attr.addFlashAttribute("tip", AjaxDTO.success(
                    SpringConfig.getString("insert.success", new Object[]{validUser.getId()}, request)));
            return "redirect:/admin/users";
        }
    }

    @DeleteMapping(value = "/admin/user/{userId}", produces = {"application/json;charset=UTF-8"})
    @Transactional
    @ResponseBody
    public String delete(@PathVariable("userId") Long userId, HttpServletRequest request) {
        int record = userService.deleteByKey(userId);
        AjaxDTO ajaxDTO;
        if (record > 0) {
            ajaxDTO = AjaxDTO.success(SpringConfig.getString("delete.success", new Object[]{userId}, request));
        } else {
            ajaxDTO = AjaxDTO.failure(SpringConfig.getString("delete.failure", new Object[]{userId}, request));
        }
        return JSONObject.toJSONString(ajaxDTO);
    }
    //----------------------POST------------------//


    //----------------------GET------------------//
    @GetMapping("/admin/user")
    public String add(Model model) {
        model.addAttribute(UserDO.TAG, new UserDO());
        Map<RoleDO, Boolean> roles = roleService.getSelectedRole(-1L);
        model.addAttribute("role", roles);
        return "admin/user/user_add";
    }

    @GetMapping("/admin/user/{userId}")
    public ModelAndView modify(@PathVariable(value = "userId") Long userId) {
        ModelAndView mv = new ModelAndView("admin/user/user_modify");
        UserDO user = userService.selectByKey(userId);
        mv.addObject(UserDO.TAG, user);

        Map<RoleDO, Boolean> roles = roleService.getSelectedRole(userId);
        mv.addObject("role", roles);
        return mv;
    }

    @GetMapping("/admin/users")
    public String all() {
        return "forward:/admin/users/page/1";
    }


    @GetMapping("/admin/users/page/{page}")
    public ModelAndView all(@PathVariable("page") Integer page) {
        ModelAndView mv = new ModelAndView("admin/user/user_all");

        // 分页大小url
        PageUrlBuilder urlBuilder = new PageUrlBuilder("/admin/users");
        DropDownListDTO pageSizeDTO = userService.getPageSize(urlBuilder);
        mv.addObject("size", pageSizeDTO);

        // 表格数据
        TableDTO<UserDO> tableDTO = new TableDTO<>();
        tableDTO.setHeader(Stream.of("用户Id", "用户名", "上次登录时间", "上次登录Ip",
                "电子邮箱", "创建时间", "上次修改时间", "操作").collect(Collectors.toList()));
        tableDTO.setSort(PageContext.getSort());
        tableDTO.setCell(userService.getByPage(page));
        mv.addObject("table", tableDTO);

        // 分页器
        int count = userService.getAllCount();
        PageDTO pageDTO = new PageDTO(count, page, "/admin/users");
        mv.addObject("page", pageDTO);

        return mv;
    }
    //----------------------GET------------------//
}

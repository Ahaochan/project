package com.ahao.invoice.admin.role.controller;

import com.ahao.config.SpringConfig;
import com.ahao.context.PageContext;
import com.ahao.entity.AjaxDTO;
import com.ahao.entity.DropDownListDTO;
import com.ahao.entity.PageDTO;
import com.ahao.entity.TableDTO;
import com.ahao.invoice.admin.auth.entity.AuthDO;
import com.ahao.invoice.admin.auth.service.AuthService;
import com.ahao.invoice.admin.role.entity.RoleDO;
import com.ahao.invoice.admin.role.service.RoleService;
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
 * Created by Avalon on 2017/6/3.
 * <p>
 * 角色的Controller层
 */
@Controller
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    private RoleService roleService;
    private AuthService authService;

    @Autowired
    public RoleController(RoleService roleService, AuthService authService) {
        this.roleService = roleService;
        this.authService = authService;
    }

    //----------------------POST------------------//
    @PostMapping("/admin/role/{roleId}")
    @Transactional
    public ModelAndView modify(HttpServletRequest request,
                               @RequestParam(name = "auth", defaultValue = "") String[] authIds,
                               @Valid RoleDO validRole, BindingResult result) {
        ModelAndView mv = new ModelAndView("admin/role/role_modify");

        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                logger.debug("验证错误: " + e.getDefaultMessage());
            }
            mv.addObject("tip", AjaxDTO.failure(
                    SpringConfig.getString("modify.failure", request)));
            Map<AuthDO, Boolean> auths = authService.getSelectedAuth(authIds);
            mv.addObject("auth", auths);
        } else {
            roleService.update(validRole);
            authService.addRelate(validRole.getId(), authIds);

            mv.addObject("tip", AjaxDTO.success(
                    SpringConfig.getString("modify.success", request)));
            Map<AuthDO, Boolean> auths = authService.getSelectedAuth(validRole.getId());
            mv.addObject("auth", auths);
        }
        return mv;
    }

    @PostMapping("/admin/role")
    @Transactional
    public String add(HttpServletRequest request,
                      @RequestParam(name = "auth", defaultValue = "") String[] authIds,
                      @Valid RoleDO validRole, BindingResult result,
                      Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                logger.debug("验证错误: " + e.getDefaultMessage());
            }
            model.addAttribute("tip", AjaxDTO.failure(
                    SpringConfig.getString("insert.failure", new Object[]{validRole.getId()}, request)));
            Map<AuthDO, Boolean> auths = authService.getSelectedAuth(authIds);
            model.addAttribute("auth", auths);
            return "admin/role/role_add";
        } else {
            roleService.insert(validRole);
            authService.addRelate(validRole.getId(), authIds);
            attr.addFlashAttribute("tip", AjaxDTO.success(
                    SpringConfig.getString("insert.success", new Object[]{validRole.getId()}, request)));
            return "redirect:/admin/roles";
        }
    }

    @DeleteMapping(value = "/admin/role/{roleId}", produces = {"application/json;charset=UTF-8"})
    @Transactional
    @ResponseBody
    public String delete(@PathVariable("roleId") Long roleId, HttpServletRequest request) {
        int record = roleService.deleteByKey(roleId);
        AjaxDTO ajaxDTO;
        if (record > 0) {
            ajaxDTO = AjaxDTO.success(SpringConfig.getString("delete.success", new Object[]{roleId}, request));
        } else {
            ajaxDTO = AjaxDTO.failure(SpringConfig.getString("delete.failure", new Object[]{roleId}, request));
        }
        return JSONObject.toJSONString(ajaxDTO);
    }
    //----------------------POST------------------//


    //----------------------GET------------------//
    @GetMapping("/admin/role")
    public String add(Model model) {
        model.addAttribute(RoleDO.TAG, new RoleDO());
        Map<AuthDO, Boolean> auths = authService.getSelectedAuth(-1L);
        model.addAttribute("auth", auths);
        return "admin/role/role_add";
    }

    @GetMapping("/admin/role/{roleId}")
    public ModelAndView modify(@PathVariable(value = "roleId") Long roleId) {
        ModelAndView mv = new ModelAndView("admin/role/role_modify");
        RoleDO role = roleService.selectByKey(roleId);
        mv.addObject(RoleDO.TAG, role);

        Map<AuthDO, Boolean> auths = authService.getSelectedAuth(roleId);
        mv.addObject("auth", auths);
        return mv;
    }

    @GetMapping("/admin/roles")
    public String all() {
        return "forward:/admin/roles/page/1";
    }


    @GetMapping("/admin/roles/page/{page}")
    public ModelAndView all(@PathVariable("page") Integer page) {
        ModelAndView mv = new ModelAndView("admin/role/role_all");

        // 分页大小url
        PageUrlBuilder urlBuilder = new PageUrlBuilder("/admin/roles");
        DropDownListDTO pageSizeDTO = roleService.getPageSize(urlBuilder);
        mv.addObject("size", pageSizeDTO);

        // 表格数据
        TableDTO<RoleDO> tableDTO = new TableDTO<>();
        tableDTO.setHeader(Stream.of("角色Id", "用户名", "描述",
                "创建时间", "上次修改时间", "操作").collect(Collectors.toList()));
        tableDTO.setSort(PageContext.getSort());
        tableDTO.setCell(roleService.getByPage(page));
        mv.addObject("table", tableDTO);

        // 分页器
        int count = roleService.getAllCount();
        PageDTO pageDTO = new PageDTO(count, page, "/admin/roles");
        mv.addObject("page", pageDTO);

        return mv;
    }
    //----------------------GET------------------//
}

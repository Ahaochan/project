package com.ahao.invoice.admin.auth.controller;

import com.ahao.config.SpringConfig;
import com.ahao.context.PageContext;
import com.ahao.entity.AjaxDTO;
import com.ahao.entity.DropDownListDTO;
import com.ahao.entity.PageDTO;
import com.ahao.entity.TableDTO;
import com.ahao.invoice.admin.auth.entity.AuthDO;
import com.ahao.invoice.admin.auth.service.AuthService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Avalon on 2017/6/7.
 */
@Controller
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //----------------------POST------------------//
    @PostMapping("/admin/auth/{authId}")
    @Transactional
    public ModelAndView modify(HttpServletRequest request,
                               @Valid AuthDO validAuth, BindingResult result) {
        ModelAndView mv = new ModelAndView("admin/auth/auth_modify");

        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                logger.debug("验证错误: " + e.getDefaultMessage());
            }
            mv.addObject("tip", AjaxDTO.failure(
                    SpringConfig.getString("modify.failure", request)));
        } else {
            authService.update(validAuth);
            mv.addObject("tip", AjaxDTO.success(
                    SpringConfig.getString("modify.success", request)));
        }
        return mv;
    }

    @PostMapping("/admin/auth")
    @Transactional
    public String add(HttpServletRequest request,
                      @Valid AuthDO validAuth, BindingResult result,
                      Model model, RedirectAttributes attr) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                logger.debug("验证错误: " + e.getDefaultMessage());
            }
            model.addAttribute("tip", AjaxDTO.failure(
                    SpringConfig.getString("insert.failure", new Object[]{validAuth.getId()}, request)));
            return "admin/auth/auth_add";
        } else {
            authService.insert(validAuth);
            attr.addFlashAttribute("tip", AjaxDTO.success(
                    SpringConfig.getString("insert.success", new Object[]{validAuth.getId()}, request)));
            return "redirect:/admin/auths";
        }
    }

    @DeleteMapping(value = "/admin/auth/{authId}", produces = {"application/json;charset=UTF-8"})
    @Transactional
    @ResponseBody
    public String delete(@PathVariable("authId") Long authId, HttpServletRequest request) {
        int record = authService.deleteByKey(authId);
        AjaxDTO ajaxDTO;
        if (record > 0) {
            ajaxDTO = AjaxDTO.success(SpringConfig.getString("delete.success", new Object[]{authId}, request));
        } else {
            ajaxDTO = AjaxDTO.failure(SpringConfig.getString("delete.failure", new Object[]{authId}, request));
        }
        return JSONObject.toJSONString(ajaxDTO);
    }
    //----------------------POST------------------//


    //----------------------GET------------------//
    @GetMapping("/admin/auth")
    public String add(Model model) {
        model.addAttribute(AuthDO.TAG, new AuthDO());
        return "admin/auth/auth_add";
    }

    @GetMapping("/admin/auth/{authId}")
    public ModelAndView modify(@PathVariable(value = "authId") Long authId) {
        ModelAndView mv = new ModelAndView("admin/auth/auth_modify");
        AuthDO auth = authService.selectByKey(authId);
        mv.addObject(AuthDO.TAG, auth);
        return mv;
    }

    @GetMapping("/admin/auths")
    public String all() {
        return "forward:/admin/auths/page/1";
    }

    @GetMapping("/admin/auths/page/{page}")
    public ModelAndView all(@PathVariable("page") Integer page) {
        ModelAndView mv = new ModelAndView("admin/auth/auth_all");

        // 分页大小url
        PageUrlBuilder urlBuilder = new PageUrlBuilder("/admin/auths");
        DropDownListDTO pageSizeDTO = authService.getPageSize(urlBuilder);
        mv.addObject("size", pageSizeDTO);

        // 表格数据
        TableDTO<AuthDO> tableDTO = new TableDTO<>();
        tableDTO.setHeader(Stream.of("权限Id", "权限名", "描述",
                "创建时间", "上次修改时间", "操作").collect(Collectors.toList()));
        tableDTO.setSort(PageContext.getSort());
        tableDTO.setCell(authService.getByPage(page));
        mv.addObject("table", tableDTO);

        // 分页器
        int count = authService.getAllCount();
        PageDTO pageDTO = new PageDTO(count, page, "/admin/auths");
        mv.addObject("page", pageDTO);

        return mv;
    }
    //----------------------forward------------------//
}

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
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

    @GetMapping("/admin/auths/page/{page}")
    public ModelAndView allAuth(@PathVariable("page") Integer page) {
        ModelAndView mv = new ModelAndView("admin/auth/auth_all");

        // 分页大小url
        PageUrlBuilder urlBuilder = new PageUrlBuilder("/admin/auths");
        DropDownListDTO pageSizeDTO = authService.getPageSize(page, urlBuilder);
        mv.addObject("size", pageSizeDTO);

        // 表格数据
        TableDTO<AuthDO> tableDTO = new TableDTO<>();
        tableDTO.setHeader(Stream.of("权限Id","权限名","描述",
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

    @GetMapping("/admin/auth/{authId}")
    public String authModify(@PathVariable(value = "authId") Long authId, Model model) {
        if(!model.containsAttribute(AuthDO.TAG)){
            AuthDO auth = authService.selectByKey(authId);
            model.addAttribute(AuthDO.TAG, auth);
        }
        Map<AuthDO, Boolean> auths = authService.getSelectedAuth(authId);
        model.addAttribute("auth", auths);
        return "admin/auth/auth_modify";
    }

    @PostMapping("/admin/auth/{authId}")
    @Transactional
    public String authModify(HttpServletRequest request,
                             @PathVariable("authId") Long authId,
                             @Valid @ModelAttribute(AuthDO.TAG) AuthDO validAuth, BindingResult result,
                             RedirectAttributes redirectAttrs
    ) {
        if (result.hasErrors()) {
            for(ObjectError e : result.getAllErrors()){
                logger.debug("验证错误: "+ e.getDefaultMessage());
            }
            validAuth.setId(authId);
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult." + AuthDO.TAG, result);
            redirectAttrs.addFlashAttribute(AuthDO.TAG, validAuth);

            redirectAttrs.addFlashAttribute("tip",
                    AjaxDTO.getFailure(SpringConfig.getString("modify.failure", request)));
            return "redirect:/admin/auth/" + authId;
        }

        authService.update(validAuth);
        redirectAttrs.addFlashAttribute("tip",
                AjaxDTO.getSuccess(SpringConfig.getString("modify.success", request)));

        return "redirect:/admin/auth/" + authId;
    }


    //----------------------forward------------------//
    @GetMapping("/admin/auths")
    public String allAuth() {
        return "forward:/admin/auths/page/1";
    }
    //----------------------forward------------------//
}

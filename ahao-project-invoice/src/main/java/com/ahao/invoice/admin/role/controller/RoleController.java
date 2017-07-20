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

    @GetMapping("/admin/roles/page/{page}")
    public ModelAndView allRole(@PathVariable("page") Integer page) {
        ModelAndView mv = new ModelAndView("admin/role/role_all");

        // 分页大小url
        PageUrlBuilder urlBuilder = new PageUrlBuilder("/admin/roles");
        DropDownListDTO pageSizeDTO = roleService.getPageSize(page, urlBuilder);
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

    @GetMapping("/admin/role/{roleId}")
    public String roleModify(@PathVariable(value = "roleId") Long roleId, Model model) {
        if (!model.containsAttribute(RoleDO.TAG)) {
            RoleDO role = roleService.selectByKey(roleId);
            model.addAttribute(RoleDO.TAG, role);
        }
        Map<AuthDO, Boolean> auths = authService.getSelectedAuth(roleId);
        model.addAttribute("auth", auths);
        return "admin/role/role_modify";
    }

    @PostMapping("/admin/role/{roleId}")
    @Transactional
    public String roleModify(HttpServletRequest request,
                             @PathVariable("roleId") Long roleId,
                             @RequestParam(name = "auth", defaultValue = "") String[] authIds,
                             @Valid @ModelAttribute(RoleDO.TAG) RoleDO validRole, BindingResult result,
                             RedirectAttributes redirectAttrs
    ) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                logger.debug("验证错误: " + e.getDefaultMessage());
            }
            validRole.setId(roleId);
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult." + RoleDO.TAG, result);
            redirectAttrs.addFlashAttribute(RoleDO.TAG, validRole);

            redirectAttrs.addFlashAttribute("tip",
                    AjaxDTO.getFailure(SpringConfig.getString("modify.failure", request)));
            return "redirect:/admin/role/" + roleId;
        }

        Long[] ids = Stream.of(authIds).map(Long::new).toArray(Long[]::new);
        roleService.update(validRole);
        authService.addRelate(roleId, ids);
        redirectAttrs.addFlashAttribute("tip",
                AjaxDTO.getSuccess(SpringConfig.getString("modify.success", request)));

        return "redirect:/admin/role/" + roleId;
    }

//    @DeleteMapping(value = "/admin/role/{roleId}")
//    @ResponseBody
//    public AjaxResult<Boolean> deleteById(@PathVariable("roleId") Long roleId){
//        int success = roleService.deleteById(roleId);
//        AjaxResult<Boolean> result;
//        if(success!=-1){
//            result = new AjaxResult<>(AjaxResult.AJAX_STATUS_CODE_SUCCESS, AjaxConsts.MSG_DELETE_SUCCESS, true);
//        } else{
//            result = new AjaxResult<>(AjaxResult.AJAX_STATUS_CODE_ERROR, AjaxConsts.MSG_DELETE_FAIL, false);
//        }
//        return result;
//    }

    //----------------------forward------------------//
    @GetMapping("/admin/roles")
    public String allRole() {
        return "forward:/admin/roles/page/1";
    }
    //----------------------forward------------------//
}

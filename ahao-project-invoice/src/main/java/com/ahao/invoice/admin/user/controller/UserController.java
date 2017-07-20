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
import com.ahao.invoice.admin.user.entity.UserDetailView;
import com.ahao.invoice.admin.user.service.UserService;
import com.ahao.util.JSONHelper;
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
import java.util.Set;
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

    @GetMapping("/admin/users/page/{page}")
    public ModelAndView allUser(@PathVariable("page") Integer page) {
        ModelAndView mv = new ModelAndView("admin/user/user_all");

        // 分页大小url
        PageUrlBuilder urlBuilder = new PageUrlBuilder("/admin/users");
        DropDownListDTO pageSizeDTO = userService.getPageSize(page, urlBuilder);
        mv.addObject("size", pageSizeDTO);

        // 表格数据
        TableDTO<UserDO> tableDTO = new TableDTO<>();
        tableDTO.setHeader(Stream.of("用户Id","用户名","上次登录时间", "上次登录Ip",
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

    @GetMapping("/admin/user/{userId}")
    public String userModify(@PathVariable(value = "userId") Long userId, Model model) {
        if(!model.containsAttribute(UserDO.TAG)){
            UserDO user = userService.selectByKey(userId);
            model.addAttribute(UserDO.TAG, user);
        }
        Map<RoleDO, Boolean> roles = roleService.getSelectedRole(userId);
        model.addAttribute("role", roles);
        return "admin/user/user_modify";
    }

    @PostMapping("/admin/user/{userId}")
    @Transactional
    public String userModify(HttpServletRequest request,
                             @PathVariable("userId") Long userId,
                             @RequestParam(name = "role", defaultValue = "") String[] roleIds,
                             @Valid @ModelAttribute(UserDO.TAG) UserDO validUser, BindingResult result,
                             RedirectAttributes redirectAttrs
    ) {
        if (result.hasErrors()) {
            for(ObjectError e : result.getAllErrors()){
                logger.debug("验证错误: "+ e.getDefaultMessage());
            }
            validUser.setId(userId);
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult." + UserDO.TAG, result);
            redirectAttrs.addFlashAttribute(UserDO.TAG, validUser);

            redirectAttrs.addFlashAttribute("tip",
                    AjaxDTO.getFailure(SpringConfig.getString("modify.failure", request)));
            return "redirect:/admin/user/" + userId;
        }

        Long[] ids = Stream.of(roleIds).map(Long::new).toArray(Long[]::new);
        userService.update(validUser);
        roleService.addRelate(userId, ids);
        redirectAttrs.addFlashAttribute("tip",
                AjaxDTO.getSuccess(SpringConfig.getString("modify.success", request)));

        return "redirect:/admin/user/" + userId;
    }

//    @DeleteMapping(value = "/admin/user/{userId}")
//    @ResponseBody
//    public AjaxResult<Boolean> deleteById(@PathVariable("userId") Long userId) {
//        //TODO 删除用户
//        int success = userService.deleteById(userId);
//        AjaxResult<Boolean> result;
//        if (success != -1) {
//            result = new AjaxResult<>(AjaxResult.AJAX_STATUS_CODE_SUCCESS, AjaxConsts.MSG_DELETE_SUCCESS, true);
//        } else {
//            result = new AjaxResult<>(AjaxResult.AJAX_STATUS_CODE_ERROR, AjaxConsts.MSG_DELETE_FAIL, false);
//        }
//        return result;
//    }



    @GetMapping("/admin/users")
    public String home() {
        return "forward:/admin/users/page/1";
    }
}

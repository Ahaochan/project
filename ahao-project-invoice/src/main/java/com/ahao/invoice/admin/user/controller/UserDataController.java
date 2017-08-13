package com.ahao.invoice.admin.user.controller;

import com.ahao.config.SpringConfig;
import com.ahao.entity.AjaxDTO;
import com.ahao.invoice.admin.role.service.RoleService;
import com.ahao.invoice.admin.user.entity.UserDO;
import com.ahao.invoice.admin.user.service.UserService;
import com.ahao.invoice.invoice.util.ValidUtils;
import com.ahao.util.CollectionHelper;
import com.ahao.util.NumberHelper;
import com.ahao.util.StringHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * Created by Avalon on 2017/5/12.
 */
@RestController
public class UserDataController {
    private static final Logger logger = LoggerFactory.getLogger(UserDataController.class);
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserDataController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/admin/user")
    @Transactional
    public AjaxDTO add(@RequestParam(name = "role[]", defaultValue = "") Long[] roleIds,
                       @Valid UserDO validUser, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("insert.failure", validUser.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        AjaxDTO ajax = AjaxDTO.success(SpringConfig.getString("insert.success", validUser.getId()));
        userService.insert(validUser);
        roleService.addRelate(validUser.getId(), roleIds);
        return ajax;
    }

    @DeleteMapping(value = "/admin/users")
    @Transactional
    public AjaxDTO delete(@RequestBody MultiValueMap<String, String> formData) {
        List<String> ids = formData.get("userIds[]");

        boolean success = userService.deleteByKey(ids);
        int flag = NumberHelper.parse(success);
        String msg = SpringConfig.getString(success ? "delete.success" : "delete.failure");
        return AjaxDTO.get(flag, msg);
    }

    @GetMapping(value = "/admin/users/page")
    public JSONObject getByPage(Integer page) {
        JSONObject json = new JSONObject();
        json.put("total", userService.getAllCount());
        json.put("rows", userService.getByPage(page));
        return json;
    }

    @PostMapping("/admin/user/{userId}")
    @Transactional
    public AjaxDTO modify(@RequestParam(name = "role[]", defaultValue = "") Long[] roleIds,
                          @Valid UserDO validUser, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("update.failure", validUser.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        if (validUser.getId() == null) {
            return AjaxDTO.failure(SpringConfig.getString("update.failure", validUser.getId()));
        }

        AjaxDTO ajax = AjaxDTO.success(SpringConfig.getString("update.success", validUser.getId()));
        userService.update(validUser);
        roleService.addRelate(validUser.getId(), roleIds);
        return ajax;
    }

    @PostMapping("/admin/user/checkUsername")
    public boolean checkUsername(String oldUsername, String username) {
        if (StringHelper.equals(username, oldUsername)) {
            return true;
        }

        boolean isExist = userService.existUsername(username);
        return !isExist;
    }

    @GetMapping("/admin/user/roles")
    public AjaxDTO getAllRoleName(Long userId) {
        JSONArray selectedRole = roleService.getSelectedRole(userId);
        if (CollectionHelper.isEmpty(selectedRole)) {
            return AjaxDTO.failure("");
        } else {
            return AjaxDTO.success(selectedRole);
        }
    }
}

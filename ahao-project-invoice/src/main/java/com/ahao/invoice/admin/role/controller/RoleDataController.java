package com.ahao.invoice.admin.role.controller;

import com.ahao.config.SpringConfig;
import com.ahao.entity.AjaxDTO;
import com.ahao.invoice.admin.auth.service.AuthService;
import com.ahao.invoice.admin.role.entity.RoleDO;
import com.ahao.invoice.admin.role.service.RoleService;
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
 * Created by Avalon on 2017/6/3.
 * <p>
 * 角色的Controller层
 */
@RestController
public class RoleDataController {
    private static final Logger logger = LoggerFactory.getLogger(RoleDataController.class);
    private RoleService roleService;
    private AuthService authService;

    @Autowired
    public RoleDataController(RoleService roleService, AuthService authService) {
        this.roleService = roleService;
        this.authService = authService;
    }

    @PostMapping("/admin/role")
    @Transactional
    public AjaxDTO add(@RequestParam(name = "auth[]", defaultValue = "") Long[] authIds,
                       @Valid RoleDO validRole, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("insert.failure", validRole.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        AjaxDTO ajax = AjaxDTO.success(SpringConfig.getString("insert.success", validRole.getId()));
        roleService.insert(validRole);
        authService.addRelate(validRole.getId(), authIds);
        return ajax;
    }

    @DeleteMapping(value = "/admin/roles")
    @Transactional
    public AjaxDTO delete(@RequestBody MultiValueMap<String, String> formData) {
        List<String> ids = formData.get("roleIds[]");

        boolean success = roleService.deleteByKey(ids);
        int flag = NumberHelper.parse(success);
        String msg = SpringConfig.getString(success ? "delete.success" : "delete.failure");
        return AjaxDTO.get(flag, msg);
    }

    @GetMapping(value = "/admin/roles/page")
    public JSONObject getByPage(Integer page) {
        JSONObject json = new JSONObject();
        json.put("total", roleService.getAllCount());
        json.put("rows", roleService.getByPage(page));
        return json;
    }

    @PostMapping("/admin/role/{roleId}")
    @Transactional
    public AjaxDTO modify(@RequestParam(name = "auth[]", defaultValue = "") Long[] authIds,
                          @Valid RoleDO validRole, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("insert.failure", validRole.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        if (validRole.getId() == null) {
            return AjaxDTO.failure(SpringConfig.getString("update.failure", validRole.getId()));
        }

        AjaxDTO ajax = AjaxDTO.success(SpringConfig.getString("insert.success", validRole.getId()));
        roleService.update(validRole);
        authService.addRelate(validRole.getId(), authIds);
        return ajax;
    }

    @PostMapping("/admin/role/checkName")
    public boolean checkName(String oldName, String name) {
        if (StringHelper.equals(name, oldName)) {
            return true;
        }

        boolean isExist = roleService.existName(name);
        return !isExist;
    }

    @GetMapping("/admin/role/auths")
    public AjaxDTO getAllAuthName(Long roleId) {
        JSONArray selectedAuth = authService.getSelectedAuth(roleId);
        if (CollectionHelper.isEmpty(selectedAuth)) {
            return AjaxDTO.failure("");
        } else {
            return AjaxDTO.success(selectedAuth);
        }
    }
}

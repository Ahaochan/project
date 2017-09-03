package com.ahao.invoice.admin.auth.controller;

import com.ahao.config.SpringConfig;
import com.ahao.entity.AjaxDTO;
import com.ahao.invoice.admin.auth.entity.AuthDO;
import com.ahao.invoice.admin.auth.service.AuthService;
import com.ahao.invoice.invoice.util.ValidUtils;
import com.ahao.util.NumberHelper;
import com.ahao.util.StringHelper;
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
 * Created by Avalon on 2017/6/7.
 */
@RestController
public class AuthDataController {
    private static final Logger logger = LoggerFactory.getLogger(AuthDataController.class);

    private AuthService authService;

    @Autowired
    public AuthDataController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/admin/auth")
    @Transactional
    public AjaxDTO add(@Valid AuthDO validAuth, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("insert.failure", validAuth.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        authService.insert(validAuth);
        Long id = validAuth.getId();
        return AjaxDTO.success(SpringConfig.getString("insert.success", id), id);
    }

    @DeleteMapping("/admin/auths")
    @Transactional
    public AjaxDTO delete(@RequestBody MultiValueMap<String, String> formData) {
        List<String> ids = formData.get("authIds[]");

        boolean success = authService.deleteByKey(ids);
        int flag = NumberHelper.parse(success);
        String msg = SpringConfig.getString(success ? "delete.success" : "delete.failure");
        return AjaxDTO.get(flag, msg);
    }

    @GetMapping("/admin/auths/page")
    public JSONObject getByPage(Integer page) {
        JSONObject json = new JSONObject();
        json.put("total", authService.getAllCount());
        json.put("rows", authService.getByPage(page));
        return json;
    }

    @PostMapping("/admin/auth/{authId}")
    @Transactional
    public AjaxDTO modify(@Valid AuthDO validAuth, BindingResult result) {
        // 后端验证是否出错
        if (result.hasErrors()) {
            AjaxDTO ajax = AjaxDTO.failure(SpringConfig.getString("insert.failure", validAuth.getId()));
            // 返回以field为键, List<错误信息> 为值的Map集合
            ajax.setObj(ValidUtils.paraseErrors(result));
            return ajax;
        }
        if (validAuth.getId() == null) {
            // ID不能为空, 否则会更新全部数据
            return AjaxDTO.failure(SpringConfig.getString("update.failure", validAuth.getId()));
        }

        authService.update(validAuth);
        return AjaxDTO.success(SpringConfig.getString("insert.success", validAuth.getId()));
    }

    @PostMapping("/admin/auth/checkName")
    public boolean checkName(String oldName, String name) {
        if (StringHelper.equals(name, oldName)) {
            return true;
        }

        boolean isExist = authService.existName(name);
        return !isExist;
    }
}

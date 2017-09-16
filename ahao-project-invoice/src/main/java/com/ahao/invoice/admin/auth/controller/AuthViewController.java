package com.ahao.invoice.admin.auth.controller;

import com.ahao.invoice.admin.auth.entity.AuthDO;
import com.ahao.invoice.admin.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Ahaochan on 2017/8/13.
 */
@Controller
public class AuthViewController {
    private static final Logger logger = LoggerFactory.getLogger(AuthViewController.class);
    private AuthService authService;

    @Autowired
    public AuthViewController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/admin/auths")
    @Secured("")
    public String all() {
        return "admin/auth/list";
    }

    @GetMapping("/admin/auth")
    public String add() {
        return "admin/auth/add";
    }

    @GetMapping("/admin/auth/{authId}")
    public ModelAndView modify(@PathVariable(value = "authId") Long authId) {
        ModelAndView mv = new ModelAndView("admin/auth/modify");
        AuthDO auth = authService.selectByKey(authId);
        mv.addObject(AuthDO.TAG, auth);
        return mv;
    }

    public static void main(String[] args) {
        for(int i = 1; i <= 30; i++){
            System.out.println("INSERT INTO `admin_role__auth` VALUES ('"+i+"', '1', '"+i+"', current_timestamp, current_timestamp);");
        }
    }
}

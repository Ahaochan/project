package com.ahao.forum.guitar.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequiresGuest
    @GetMapping("/login")
    public String login(){
        return "admin/login";
    }

    @RequiresGuest
    @PostMapping("/login")
    public String login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        HttpSession session, Model model) {

        Subject currentUser = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//        token.setRememberMe(true);

        try {
            currentUser.login(token);
        } catch (AuthenticationException e){
            logger.warn("登录失败", e);
            model.addAttribute("username_tmp", username);
            model.addAttribute("password_tmp", password);
            model.addAttribute("message", e.getMessage());
            return "admin/login";
        }

        // 验证是否通过
        if (currentUser.isAuthenticated()) {
            logger.debug("登陆成功: username:"+username);
            session.setAttribute("username", username);
            return "redirect: index";
        } else {
            model.addAttribute("username_tmp", username);
            model.addAttribute("password_tmp", password);
            model.addAttribute("message", "登录失败, 用户名/密码错误");
            logger.warn("登录失败: username:" + username + ", password:" + password);
            return "admin/login";
        }
    }
}

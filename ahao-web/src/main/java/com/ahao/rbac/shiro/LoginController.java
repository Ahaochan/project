package com.ahao.rbac.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {
    @PostMapping
    public String passwordLogin(@RequestParam String username, @RequestParam String password,
                                @RequestParam(defaultValue = "false") Boolean rememberMe) {
        UsernamePasswordToken passwordToken = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(passwordToken);
        } finally {
            passwordToken.clear();
        }
        if(subject.isAuthenticated()) {
            return "ok";
        }
        return "fail";
    }

    @GetMapping("/logout")
    public Object logout() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return "当前没有登陆用户, 退出失败";
        }

        subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
        return "退出成功";
    }

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "没有权限";
    }

}

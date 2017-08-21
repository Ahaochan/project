package com.ahao.invoice.base;

import com.ahao.invoice.admin.user.service.UserService;
import com.ahao.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Avalon on 2017/6/24.
 */
@Controller
public class AppController {
    private UserService userService;

    @Autowired
    public AppController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("admin/index");
        String loginUsername = SecurityHelper.loginUsername();
        String loginIp = SecurityHelper.getClientIp();


        IndexView view = new IndexView(new Date(), loginUsername, loginIp);
        mv.addObject(IndexView.TAG, view);

        /* 更新最后一次登录时间和ip */
        userService.updateLoginMsg(loginUsername);
        return mv;
    }

    @GetMapping({"/admin/login", "/"})
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout){
        ModelAndView mv = new ModelAndView();
        User user = SecurityHelper.loginUser();
        if(user!=null) {
            mv.setViewName("redirect:/admin/index");
        } else {
            mv.setViewName("admin/login");
            if(error!=null){
                mv.addObject("error", "登录失败");
            }
            if(logout!=null){
                mv.addObject("logout", "注销成功");
            }
        }
        return mv;
    }

    @GetMapping("/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/admin/login?logout";
    }
}

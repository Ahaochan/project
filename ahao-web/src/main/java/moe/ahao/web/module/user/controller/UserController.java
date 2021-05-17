package moe.ahao.web.module.user.controller;

import moe.ahao.web.module.user.entity.User;
import moe.ahao.web.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/get")
    public User getUserInfo(@RequestParam Long id) {
        return userService.getUserById(id);
    }

}

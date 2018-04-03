package com.ahao.forum.guitar.manager.profile.controller;

import com.ahao.core.entity.AjaxDTO;
import com.ahao.forum.guitar.manager.profile.service.ProfileService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager/api")
public class ProfileApiController {

    private ProfileService profileService;

    public ProfileApiController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @RequiresAuthentication
    @PostMapping("/profile/save")
    public AjaxDTO save(@RequestParam Long userId,
                       @RequestParam(defaultValue = "") String email,
                       @RequestParam(defaultValue = "0") Integer sex,
                       @RequestParam(defaultValue = "") String qq,
                       @RequestParam(defaultValue = "") String city) {
        boolean success = profileService.saveProfile(userId, email, sex, qq, city);
        return AjaxDTO.get(success);
    }

}

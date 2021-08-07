package moe.ahao.web.module.user.controller;

import moe.ahao.web.module.user.controller.dto.UserSaveDTO;
import moe.ahao.web.module.user.entity.User;
import moe.ahao.web.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/get")
    public User getUserInfo(@RequestParam Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/add")
    public Long add(@Validated(UserSaveDTO.Add.class) @RequestBody UserSaveDTO dto) {
        Long id = userService.save(dto);
        return id;
    }

    @PostMapping("/update")
    public Long update(@Validated(UserSaveDTO.Update.class) @RequestBody UserSaveDTO dto) {
        Long id = userService.save(dto);
        return id;
    }
}

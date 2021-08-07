package moe.ahao.web.module.user.service;

import moe.ahao.web.module.user.controller.dto.UserSaveDTO;
import moe.ahao.web.module.user.entity.User;

public interface UserService {
    User getUserById(Long id);

    Long save(UserSaveDTO dto);
}

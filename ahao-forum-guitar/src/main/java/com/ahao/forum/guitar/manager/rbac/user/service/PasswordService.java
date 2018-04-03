package com.ahao.forum.guitar.manager.rbac.user.service;

public interface PasswordService {

    String modifyPassword(Long userId, String oldPassword, String newPassword);
}

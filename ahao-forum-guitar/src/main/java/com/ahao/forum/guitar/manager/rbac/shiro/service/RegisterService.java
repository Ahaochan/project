package com.ahao.forum.guitar.manager.rbac.shiro.service;

public interface RegisterService {
    boolean isExistUsername(String username);
    boolean register(String username, String password);
}

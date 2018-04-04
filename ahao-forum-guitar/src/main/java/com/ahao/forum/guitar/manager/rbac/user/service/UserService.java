package com.ahao.forum.guitar.manager.rbac.user.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface UserService {

    long saveUser(Long userId, String username, String password, Integer enabled, String email, Integer sex, String city, String qq, Long... roleId);

    int deleteUser(Long... userIds);

    List<IDataSet> getUsersTable(int weight, String search);

    IDataSet getUser(Long userId);


    int getMaxRoleWeight(Long userId);

    List<IDataSet> getSelectedRoles(Long userId);
}

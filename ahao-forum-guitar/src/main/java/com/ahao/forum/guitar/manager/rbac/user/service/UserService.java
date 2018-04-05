package com.ahao.forum.guitar.manager.rbac.user.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface UserService {
    long saveUser(Long userId, String username, String password, String email, Integer sex, String qq, String city, Integer enabled, Long roleId, Long[] categoryIds, Long[] forumIds);

    int deleteUser(Long... userIds);

    List<IDataSet> getUsersTable(int weight, String search);

    IDataSet getUser(Long userId);

    List<IDataSet> getRoles();
    List<IDataSet> getSelectedRoles(Long userId);
    List<IDataSet> getCategories();
    List<IDataSet> getSelectedCategories(Long userId);
    List<IDataSet> getForums();
    List<IDataSet> getSelectedForums(Long userId);
}

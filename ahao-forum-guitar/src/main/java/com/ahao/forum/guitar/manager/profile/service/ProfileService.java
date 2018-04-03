package com.ahao.forum.guitar.manager.profile.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface ProfileService {
    boolean saveProfile(Long userId, String email, Integer sex, String qq, String city);


    IDataSet getProfile(Long userId);
    List<IDataSet> getSelectedRole(Long userId);
    List<IDataSet> getSelectedAuth(Long userId);

}

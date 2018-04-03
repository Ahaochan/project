package com.ahao.forum.guitar.manager.profile.service.impl;

import com.ahao.core.entity.DataSet;
import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.manager.profile.dao.ProfileMapper;
import com.ahao.forum.guitar.manager.profile.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {
    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);


    private ProfileMapper profileMapper;

    public ProfileServiceImpl(ProfileMapper profileMapper) {
        this.profileMapper = profileMapper;
    }

    @Override
    public boolean saveProfile(Long userId, String email, Integer sex, String qq, String city) {
        if (userId == null || userId <= 0) {
            logger.debug("用户id非法:" + userId);
            return false;
        }
        if (sex == null || sex < 0) {
            sex = 0;
        }
        profileMapper.updateProfile(userId, email, sex, qq, city);
        return true;
    }

    @Override
    public IDataSet getProfile(Long userId) {
        if (userId == null || userId <= 0) {
            logger.debug("用户id非法:" + userId);
            return new DataSet(0);
        }
        IDataSet profile = profileMapper.getProfile(userId);
        return profile;
    }

    @Override
    public List<IDataSet> getSelectedRole(Long userId) {
        if (userId == null || userId <= 0) {
            logger.debug("用户id非法:" + userId);
            return Collections.emptyList();
        }
        List<IDataSet> roles = profileMapper.getSelectedRolesByUserId(userId);
        return roles;
    }

    @Override
    public List<IDataSet> getSelectedAuth(Long userId) {
        if (userId == null || userId <= 0) {
            logger.debug("用户id非法:" + userId);
            return Collections.emptyList();
        }
        List<IDataSet> auths = profileMapper.getSelectedAuthsByUserId(userId);
        return auths;
    }

}

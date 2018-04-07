package com.ahao.forum.guitar.manager.rbac.user.service.impl;

import com.ahao.core.config.SystemConfig;
import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.math.NumberHelper;
import com.ahao.forum.guitar.manager.rbac.shiro.util.ShiroHelper;
import com.ahao.forum.guitar.manager.rbac.user.dao.UserMapper;
import com.ahao.forum.guitar.manager.rbac.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Transactional
    @Override
    public long saveUser(Long userId, String username, String password,
                         String email, Integer sex, String qq, String city,
                         Integer enabled,
                         Long roleId, Long[] categoryIds, Long[] forumIds) {
        // 1. 数据初始化
        if (enabled == null || enabled < 0) {
            enabled = 1;
        }
        if (sex == null || sex < 0 || sex > 2) {
            sex = 0;
        }
        // 2. 如果 userId 不存在, 则插入数据
        if (userId == null || userId <= 0) {
            // 2.1. BaseDO封装插入记录的id
            BaseDO idDO = new BaseDO();
            userMapper.saveUser(idDO, username, password, enabled);
            userId = idDO.getId();
            // 2.2. 插入失败则返回 -1
            if (userId == null || userId < 0) {
                return -1;
            }
        }
        // 3. 如果 userId 存在, 则更新数据
        else {
            // 3.1. 更新数据
            boolean success = userMapper.updateUserAndProfile(userId, password, enabled, email, sex, qq, city);
        }

        // 4. 添加用户角色关联
        userMapper.relateUserRole(userId, roleId);

        // 5. 添加用户分区关联
        if(roleId == SystemConfig.instance().getInt("role.super-moderator", "id")){
            userMapper.relateUserCategory(userId, NumberHelper.unboxing(categoryIds));
        }

        // 6. 添加用户板块关联
        if(roleId == SystemConfig.instance().getInt("role.moderator", "id")){
            userMapper.relateUserForum(userId, NumberHelper.unboxing(forumIds));
        }
        return userId;
    }

    @Override
    public int deleteUser(Long... userIds) {
        // 1. 判断至少有一条记录存在
        boolean oneExist = false;
        for (Long userId : userIds) {
            if (userId == null || userId <= 0) {
                continue;
            }
            boolean exists = userMapper.checkUserExists(userId);
            if (exists) {
                oneExist = true;
                break;
            }
        }
        // 2. 如果存在, 则删除选择的数据
        if (oneExist) {
            int deleteCount = userMapper.deleteUser(NumberHelper.unboxing(userIds));
            return deleteCount;
        }
        // 3. 如果不存在, 则返回0
        logger.debug("删除用户失败, 数据表中不存在id:" + Arrays.toString(userIds) + "的记录");
        return 0;
    }

    @Override
    public List<IDataSet> getUsersTable(int weight, String search) {
        List<IDataSet> list = userMapper.getUsersTableByWeight(weight, search);
        return list;
    }

    @Override
    public IDataSet getUser(Long userId) {
        if (userId == null || userId <= 0) {
            logger.debug("用户id非法:" + userId);
            return null;
        }
        IDataSet data = userMapper.getUser(userId);
        return data;
    }

    @Override
    public List<IDataSet> getRoles() {
        // 1. 获取已登录的权值
        int weight = ShiroHelper.getMyUserWeight();

        // 2. 获取小于已登录用户权值的角色
        return userMapper.getSelectedRoles(-1, weight);
    }

    @Override
    public List<IDataSet> getSelectedRoles(Long userId) {
        if (userId == null || userId <= 0) {
            logger.debug("用户id非法:" + userId);
            return null;
        }
        // 1. 获取已登录的权值
        int weight = ShiroHelper.getMyUserWeight();

        // 2. 获取小于已登录用户权值的角色
        return userMapper.getSelectedRoles(userId, weight);
    }

    @Override
    public List<IDataSet> getCategories() {
        return userMapper.getSelectedCategories(-1);
    }

    @Override
    public List<IDataSet> getSelectedCategories(Long userId) {
        if (userId == null || userId <= 0) {
            logger.debug("用户id非法:" + userId);
            return null;
        }
        List<IDataSet> list = userMapper.getSelectedCategories(userId);
        return list;
    }

    @Override
    public List<IDataSet> getForums() {
        return userMapper.getSelectedForums(-1, ShiroHelper.isRoot(), ShiroHelper.getMyUserId());
    }

    @Override
    public List<IDataSet> getSelectedForums(Long userId) {
        if (userId == null || userId <= 0) {
            logger.debug("用户id非法:" + userId);
            return null;
        }
        boolean showAll = userMapper.getUser(userId).getInt("weight") < ShiroHelper.getMyUserWeight();
        List<IDataSet> list = userMapper.getSelectedForums(userId, showAll, ShiroHelper.getMyUserId());
        return list;
    }
}

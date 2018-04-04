package com.ahao.forum.guitar.manager.rbac.user.service.impl;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.manager.rbac.user.dao.UserMapper;
import com.ahao.forum.guitar.manager.rbac.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public long saveUser(Long userId, String username, String password, Integer enabled,
                         String email, Integer sex, String city, String qq, Long... roleId) {
//        // 1. 数据初始化
//        if (enabled == null || enabled < 0) {
//            enabled = 1;
//        }
//        if (sex == null || sex < 0 || sex > 2) {
//            sex = 0;
//        }
//        // 2. 如果 categoryId 不存在, 则插入数据, 并添加联系
//        if (userId == null || userId <= 0) {
//            // 2.1. BaseDO封装插入记录的id
//            BaseDO idDO = new BaseDO();
//            userMapper.saveUser(idDO, username, password, enabled);
//            Long id = idDO.getId();
//            // 2.2. 插入失败则返回 false
//            if (id == null || id < 0) {
//                return -1;
//            }
//            // 2.3. 插入成功则添加联系
//            else {
//                categoryMapper.relateCategoryForum(id, NumberHelper.unboxing(forumIds));
//                categoryId = id;
//            }
//        }
//        // 3. 如果 categoryId 存在, 则更新数据, 并添加联系
//        else {
//            // 3.1. 更新数据
//            boolean success = categoryMapper.updateCategory(categoryId, name, description, status);
//            // 3.2. 更新成功则添加联系
//            categoryMapper.relateCategoryForum(categoryId, NumberHelper.unboxing(forumIds));
//        }
//
//        // 4. 为所有最高管理员添加该分区的联系
//        categoryMapper.relateRootCategory(categoryId);
//        return categoryId;
        return 0;
    }

    @Override
    public int deleteUser(Long... userIds) {
//        // 1. 判断至少有一条记录存在
//        boolean oneExist = false;
//        for (Long userId : userIds) {
//            if (userId == null || userId <= 0) {
//                continue;
//            }
//            IDataSet data = userMapper.getUserById(userId, "id");
//            if (data != null) {
//                oneExist = true;
//                break;
//            }
//        }
//        // 2. 如果存在, 则删除选择的数据
//        if (oneExist) {
//            int deleteCount = userMapper.deleteUser(NumberHelper.unboxing(userIds));
//            return deleteCount;
//        }
//        // 3. 如果不存在, 则返回0
//        logger.debug("删除分区失败, 数据表中不存在id:" + Arrays.toString(userIds) + "的记录");
        return 0;
    }

    @Override
    public int getMaxRoleWeight(Long userId) {
        if (userId == null || userId <= 0) {
            logger.debug("用户id非法:" + userId);
            return 0;
        }
        int weight = userMapper.getMaxWeightByUserId(userId);
        return weight;
    }

    @Override
    public List<IDataSet> getUsersTable(int weight, String search) {
        List<IDataSet> list = userMapper.getUsersTableByWeight(weight, search);
        return list;
    }

    @Override
    public IDataSet getUser(Long userId) {
//        if (categoryId == null || categoryId <= 0) {
//            logger.debug("板块id非法:" + categoryId);
//            return null;
//        }
//        IDataSet data = categoryMapper.getCategoryById(categoryId);
//        return data;
        return null;
    }

    @Override
    public List<IDataSet> getSelectedRoles(Long userId) {
//        if (categoryId == null || categoryId <= 0) {
//            logger.debug("板块id非法:" + categoryId);
//            return null;
//        }
//        List<IDataSet> list = categoryMapper.getSelectedForumsByCategoryId(categoryId);
//        return list;
        return null;
    }
}

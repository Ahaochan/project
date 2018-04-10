package com.ahao.forum.guitar.manager.forum.service.impl;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.CollectionHelper;
import com.ahao.core.util.lang.math.NumberHelper;
import com.ahao.forum.guitar.manager.forum.dao.ForumMapper;
import com.ahao.forum.guitar.manager.forum.service.ForumService;
import com.ahao.forum.guitar.manager.rbac.shiro.util.ShiroHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ForumServiceImpl implements ForumService{
    private static final Logger logger = LoggerFactory.getLogger(ForumServiceImpl.class);

    private ForumMapper forumMapper;

    @Autowired
    public ForumServiceImpl(ForumMapper forumMapper) {
        this.forumMapper = forumMapper;
    }

    @Transactional
    @Override
    public long saveForum(Long forumId, String name, String description, Integer status, String iconUrl) {
        // 1. 数据初始化
        if (status == null || status < 0) {
            status = 1;
        }
        // 2. 如果 forumId 不存在, 则插入数据
        if (forumId == null || forumId <= 0) {
            // 2.1. BaseDO封装插入记录的id
            BaseDO idDO = new BaseDO();
            forumMapper.saveForum(idDO, name, description, status, iconUrl);
            forumId = idDO.getId();
            // 2.2. 插入失败则返回 false
            if (forumId == null || forumId < 0) {
                return -1;
            }
        }
        // 3. 如果 forumId 存在, 则更新数据
        else {
            // 3.1. 更新数据
            boolean success = forumMapper.updateForum(forumId, name, description, status, iconUrl);
        }
        return forumId;
    }

    @Override
    public boolean deleteForum(Long... forumIds) {
        // 1. 判断至少有一条记录存在
        boolean oneExist = false;
        for (Long forumId : forumIds) {
            if (forumId == null || forumId <= 0) {
                continue;
            }
            IDataSet data = forumMapper.getForumById(forumId, "id");
            if (data != null) {
                oneExist = true;
                break;
            }
        }
        // 2. 如果存在, 则删除选择的数据
        if (oneExist) {
            int deleteCount = forumMapper.deleteForum(NumberHelper.unboxing(forumIds));
            return true;
        }
        // 3. 如果不存在, 则返回0
        logger.debug("删除板块失败, 数据表中不存在id:" + Arrays.toString(forumIds) + "的记录");
        return false;
    }

    @Override
    public List<IDataSet> getCategories(Long forumId) {
        if (forumId == null || forumId <= 0) {
            logger.debug("板块id非法:" + forumId);
            return Collections.emptyList();
        }
//        List<IDataSet> list = forumMapper.getCategoriesByForumId(forumId);
        return null;
    }

    @Override
    public IDataSet getForum(Long forumId) {
        if (forumId == null || forumId <= 0) {
            logger.debug("板块id非法:" + forumId);
            return null;
        }
        return forumMapper.getForumById(forumId);
    }

    @Override
    public List<IDataSet> getForums(Long userId, String search) {
        // 1. 最高管理员 获取全部的板块
        if(ShiroHelper.isRoot()){
            return forumMapper.getForums(search);
        }
        // 2. 分区版主 获取拥有的分区下属的板块
        if(ShiroHelper.hasAllRoles("role.supermoderator")){
            return forumMapper.getForumsBySuperModerator(userId, search);
        }
        // 3. 版主 获取拥有的板块
        if(ShiroHelper.hasAllRoles("role.moderator")){
            return forumMapper.getForumsByModerator(userId, search);
        }

        return CollectionHelper.emptyList();
    }
}

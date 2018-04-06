package com.ahao.forum.guitar.manager.forum.service.impl;

import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.math.NumberHelper;
import com.ahao.forum.guitar.manager.forum.dao.ForumMapper;
import com.ahao.forum.guitar.manager.forum.service.ForumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public long saveForum(Long forumId, Long categoryId, String name, String description, Integer status, String iconUrl) {
        return 0;
    }

    @Override
    public int deleteForum(Long... forumIds) {
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
            return deleteCount;
        }
        // 3. 如果不存在, 则返回0
        logger.debug("删除板块失败, 数据表中不存在id:" + Arrays.toString(forumIds) + "的记录");
        return 0;
    }

    @Override
    public List<IDataSet> getCategories() {
        return null;
    }

    @Override
    public List<IDataSet> getForums(String... fields) {
        return forumMapper.getForums(fields);
    }

    @Override
    public List<IDataSet> getForumsTable(Long userId, String search) {
        if (userId == null || userId <= 0) {
            logger.debug("用户id非法:" + userId);
            return Collections.emptyList();
        }
        List<IDataSet> list = forumMapper.getForumsTable(userId, search);
        return list;
    }


}

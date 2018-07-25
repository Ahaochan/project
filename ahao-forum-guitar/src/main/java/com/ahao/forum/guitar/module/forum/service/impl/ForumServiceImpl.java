package com.ahao.forum.guitar.module.forum.service.impl;

import com.ahao.commons.entity.IDataSet;
import com.ahao.forum.guitar.module.forum.dao.ForumMapper;
import com.ahao.forum.guitar.module.forum.service.ForumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("forumForeService")
public class ForumServiceImpl implements ForumService {
    private static final Logger logger = LoggerFactory.getLogger(ForumServiceImpl.class);

    private ForumMapper forumMapper;
    public ForumServiceImpl(ForumMapper forumMapper){
        this.forumMapper = forumMapper;
    }

    @Override
    public IDataSet getForum(Long forumId) {
        if (forumId == null || forumId <= 0) {
            logger.debug("板块id非法:" + forumId);
            return null;
        }
        IDataSet data = forumMapper.getForumById(forumId);
        return data;
    }

    @Override
    public List<IDataSet> getThread(Long forumId, String search) {
        if (forumId == null || forumId <= 0) {
            logger.debug("板块id非法:" + forumId);
            return null;
        }
        List<IDataSet> list = forumMapper.getThreadByForumId(forumId, search);
        return list;
    }
}

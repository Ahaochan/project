package com.ahao.forum.guitar.module.forum.service.impl;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.module.forum.dao.ForumMapper;
import com.ahao.forum.guitar.module.forum.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumServiceImpl implements ForumService{

    private ForumMapper forumMapper;

    @Autowired
    public ForumServiceImpl(ForumMapper forumMapper) {
        this.forumMapper = forumMapper;
    }

    @Override
    public List<IDataSet> getForums(String... fields) {
        return forumMapper.getForums(fields);
    }

    @Override
    public List<IDataSet> getForums(Integer parentId) {
        return null;
    }
}

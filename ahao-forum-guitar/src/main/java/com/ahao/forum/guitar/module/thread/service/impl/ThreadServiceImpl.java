package com.ahao.forum.guitar.module.thread.service.impl;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.module.thread.dao.ThreadMapper;
import com.ahao.forum.guitar.module.thread.service.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreadServiceImpl implements ThreadService {
    private static final Logger logger = LoggerFactory.getLogger(ThreadServiceImpl.class);

    private ThreadMapper threadMapper;

    public ThreadServiceImpl(ThreadMapper threadMapper) {
        this.threadMapper = threadMapper;
    }

    @Override
    public IDataSet getThread(Long threadId) {
        if (threadId == null || threadId <= 0) {
            logger.debug("主题id非法:" + threadId);
            return null;
        }
        IDataSet data = threadMapper.getThreadById(threadId);
        return data;
    }

    @Override
    public List<IDataSet> getPosts(Long threadId) {
        if (threadId == null || threadId <= 0) {
            logger.debug("主题id非法:" + threadId);
            return null;
        }
        List<IDataSet> list = threadMapper.getPosts(threadId);
        return list;

    }
}

package com.ahao.forum.guitar.module.thread.service.impl;

import com.ahao.core.entity.BaseDO;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.time.DateHelper;
import com.ahao.forum.guitar.module.thread.dao.ThreadMapper;
import com.ahao.forum.guitar.module.thread.service.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ThreadServiceImpl implements ThreadService {
    private static final Logger logger = LoggerFactory.getLogger(ThreadServiceImpl.class);

    private ThreadMapper threadMapper;

    public ThreadServiceImpl(ThreadMapper threadMapper) {
        this.threadMapper = threadMapper;
    }

    @Override
    public long saveThread(Long threadId, String title, String content, Long userId, Long forumId) {
        // 1. 数据初始化
        if (userId == null || userId < 0) {
            logger.error("用户未登录, 发布主题失败");
            return -1;
        }
        if (forumId == null || forumId < 0) {
            logger.error("板块id非法:"+forumId);
            return -1;
        }

        // 2. 如果 threadId 不存在, 则插入数据
        if (threadId == null || threadId <= 0) {
            // 2.1. BaseDO封装插入记录的id
            BaseDO idDO = new BaseDO();
            threadMapper.saveThread(idDO, title, content, userId, forumId, new Date());
            threadId = idDO.getId();
            // 2.2. 插入失败则返回 false
            if (threadId == null || threadId < 0) {
                return -1;
            }
        }
        // 3. 如果 threadId 存在, 则更新数据
        else {
            // 3.1. 更新数据
            boolean success = threadMapper.updateThread(threadId, title, content, userId, forumId, new Date());
        }
        return threadId;
    }

//    @Override
//    public int deleteAuth(Long... authIds) {
//        // 1. 判断至少有一条记录存在
//        boolean oneExist = false;
//        for (Long authId : authIds) {
//            if (authId == null || authId <= 0) {
//                continue;
//            }
//            IDataSet data = authMapper.getAuthById(authId, "id");
//            if (data != null) {
//                oneExist = true;
//                break;
//            }
//        }
//        // 2. 如果存在, 则删除选择的数据
//        if (oneExist) {
//            int deleteCount = authMapper.deleteAuth(NumberHelper.unboxing(authIds));
//            return deleteCount;
//        }
//        // 3. 如果不存在, 则返回0
//        logger.debug("删除权限失败, 数据表中不存在id:" + Arrays.toString(authIds) + "的记录");
//        return 0;
//    }

    @Override
    public IDataSet getForum(Long forumId) {
        if (forumId == null || forumId <= 0) {
            logger.debug("板块id非法:" + forumId);
            return null;
        }
        IDataSet data = threadMapper.getForumById(forumId);
        return data;
    }

    @Override
    public IDataSet getThread(Long threadId) {
        if (threadId == null || threadId <= 0) {
            logger.debug("主题id非法:" + threadId);
            return null;
        }
        IDataSet data = threadMapper.getThreadById(threadId);
        if(data.containsKey("thread_create_time")){
            Date date = data.getDate("thread_create_time");
            data.put("thread_create_time", DateHelper.getString(date, "yyyy年MM月dd日 hh:mm:ss"));
        }
        if(data.containsKey("thread_modify_time")){
            Date date = data.getDate("thread_modify_time");
            data.put("thread_modify_time", DateHelper.getString(date, "yyyy年MM月dd日 hh:mm:ss"));
        }
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

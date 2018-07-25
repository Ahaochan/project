package com.ahao.forum.guitar.module.post.service.impl;

import com.ahao.commons.entity.BaseDO;
import com.ahao.commons.entity.IDataSet;
import com.ahao.commons.util.lang.math.NumberHelper;
import com.ahao.commons.util.lang.time.DateHelper;
import com.ahao.forum.guitar.module.post.dao.PostMapper;
import com.ahao.forum.guitar.module.post.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Service
public class PostServiceImpl implements PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    private PostMapper postMapper;

    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Override
    public long savePost(Long postId, Long prePostId, String content, Long userId, Long threadId) {
        // 1. 数据初始化
        if (userId == null || userId < 0) {
            logger.error("用户未登录, 发布主题失败");
            return -1;
        }
        if (threadId == null || threadId < 0) {
            logger.error("主题id非法:"+threadId);
            return -1;
        }

        // 2. 如果 postId 不存在, 则插入数据
        if (postId == null || postId <= 0) {
            // 2.1. BaseDO封装插入记录的id
            BaseDO idDO = new BaseDO();
            postMapper.savePost(idDO, prePostId, content, userId, threadId, new Date());
            postId = idDO.getId();
            // 2.2. 插入失败则返回 false
            if (postId == null || postId < 0) {
                return -1;
            }
        }
        // 3. 如果 postId 存在, 则更新数据
        else {
            // 3.1. 更新数据
            boolean success = postMapper.updatePost(postId, prePostId, content, userId, threadId, new Date());
        }
        return postId;
    }

    @Override
    public int deletePost(Long... postIds) {
        // 1. 判断至少有一条记录存在
        boolean oneExist = false;
        for (Long postId : postIds) {
            if (postId == null || postId <= 0) {
                continue;
            }
            IDataSet data = postMapper.getPost(postId);
            if (data != null) {
                oneExist = true;
                break;
            }
        }
        // 2. 如果存在, 则删除选择的数据
        if (oneExist) {
            int deleteCount = postMapper.deletePost(NumberHelper.unboxing(postIds));
            return deleteCount;
        }
        // 3. 如果不存在, 则返回0
        logger.debug("删除分区失败, 数据表中不存在id:" + Arrays.toString(postIds) + "的记录");
        return 0;
    }

    @Override
    public IDataSet getThreadById(Long threadId) {
        if (threadId == null || threadId <= 0) {
            logger.debug("主题id非法:" + threadId);
            return null;
        }
        IDataSet data = postMapper.getThreadById(threadId);
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
    public IDataSet getThreadByPostId(Long postId) {
        if (postId == null || postId <= 0) {
            logger.debug("回复id非法:" + postId);
            return null;
        }
        IDataSet data = postMapper.getThreadByPostId(postId);
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
    public IDataSet getPost(Long postId) {
        if (postId == null || postId <= 0) {
            logger.debug("回复id非法:" + postId);
            return null;
        }
        IDataSet data = postMapper.getPost(postId);
        return data;
    }
}

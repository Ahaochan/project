package com.ahao.forum.guitar.module.post.service.impl;

import com.ahao.core.context.PageContext;
import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.web.PageIndicator;
import com.ahao.forum.guitar.module.post.dao.PostMapper;
import com.ahao.forum.guitar.module.post.service.PostService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private PostMapper postMapper;

    @Autowired
    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Override
    public JSONObject getPosts(Long categoryId, String search, Integer page) {
        JSONObject result = new JSONObject();
        // 1. 数据初始化
        if (categoryId == null || categoryId <= 0) {
            return result;
        }
        if(page == null || page <= 0){
            page = 1;
        }
        int pageSize = PageContext.getPageSize();


        // 2. 分页获取当前板块下的帖子
        PageHelper.startPage(page, pageSize);
        List<IDataSet> list = postMapper.getPostByCategoryId(categoryId, search);
        result.put("list", list);

        // 3. 获取分页器
        PageInfo<IDataSet> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        String pageIndicator = PageIndicator.getBootstrap(total, page, pageSize);
        result.put("pageIndicator", pageIndicator);

        return result;
    }
}

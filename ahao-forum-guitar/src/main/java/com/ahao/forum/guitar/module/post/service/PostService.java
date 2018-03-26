package com.ahao.forum.guitar.module.post.service;


import com.alibaba.fastjson.JSONObject;

public interface PostService {

    JSONObject getPosts(Long categoryId, String search, Integer page);
}

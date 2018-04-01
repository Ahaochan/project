package com.ahao.forum.guitar.module.index.service.impl;

import com.ahao.core.entity.IDataSet;
import com.ahao.forum.guitar.module.index.dao.IndexMapper;
import com.ahao.forum.guitar.module.index.service.IndexService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl implements IndexService {

    private IndexMapper indexMapper;
    public IndexServiceImpl(IndexMapper indexMapper){
        this.indexMapper = indexMapper;
    }

    @Override
    public List<IDataSet> getCategories() {
        List<IDataSet> list = indexMapper.getCategories(1, "id", "name");
        return list;
    }

    @Override
    public List<IDataSet> getForums(long categoryId) {
        List<IDataSet> list = indexMapper.getForumsByCategoryId(categoryId, "id", "name");
        return list;
    }
}

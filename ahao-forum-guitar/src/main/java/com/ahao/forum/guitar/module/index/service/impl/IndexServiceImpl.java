package com.ahao.forum.guitar.module.index.service.impl;

import com.ahao.commons.entity.IDataSet;
import com.ahao.commons.spring.util.RequestHelper;
import com.ahao.forum.guitar.module.index.dao.IndexMapper;
import com.ahao.forum.guitar.module.index.service.IndexService;
import org.apache.commons.lang3.StringUtils;
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
        List<IDataSet> list = indexMapper.getCategories(1);
        return list;
    }

    @Override
    public List<IDataSet> getForums(long categoryId) {
        List<IDataSet> list = indexMapper.getForumsByCategoryId(categoryId);
        for (IDataSet data : list) {
            String url = data.getString("icon_url");
            if(!StringUtils.startsWithAny(url, "http")){
                data.put("icon_url", RequestHelper.getContextPath()+"/"+url);
            }
        }
        return list;
    }
}

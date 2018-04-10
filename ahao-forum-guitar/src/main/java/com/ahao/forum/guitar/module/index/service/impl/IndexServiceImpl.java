package com.ahao.forum.guitar.module.index.service.impl;

import com.ahao.core.entity.IDataSet;
import com.ahao.core.util.lang.StringHelper;
import com.ahao.core.util.web.RequestHelper;
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
        List<IDataSet> list = indexMapper.getCategories(1);
        return list;
    }

    @Override
    public List<IDataSet> getForums(long categoryId) {
        List<IDataSet> list = indexMapper.getForumsByCategoryId(categoryId);
        for (IDataSet data : list) {
            String url = data.getString("icon_url");
            if(!StringHelper.startsWithAny(url, "http")){
                data.put("icon_url", RequestHelper.getContextPath()+"/"+url);
            }
        }
        return list;
    }
}

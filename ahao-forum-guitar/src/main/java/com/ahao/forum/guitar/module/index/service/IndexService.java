package com.ahao.forum.guitar.module.index.service;

import com.ahao.core.entity.IDataSet;

import java.util.List;

public interface IndexService {
    List<IDataSet> getCategories();
    List<IDataSet> getForums(long categoryId);
}

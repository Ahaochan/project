package com.ahao.forum.guitar.module.index.service;

import com.ahao.commons.entity.IDataSet;

import java.util.List;

public interface IndexService {
    List<IDataSet> getCategories();
    List<IDataSet> getForums(long categoryId);
}

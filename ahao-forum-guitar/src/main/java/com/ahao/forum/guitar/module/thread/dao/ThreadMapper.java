package com.ahao.forum.guitar.module.thread.dao;

import com.ahao.core.entity.IDataSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadMapper {
    IDataSet getThreadById(@Param("threadId") long threadId);

    List<IDataSet> getPosts(@Param("threadId") long threadId);
}

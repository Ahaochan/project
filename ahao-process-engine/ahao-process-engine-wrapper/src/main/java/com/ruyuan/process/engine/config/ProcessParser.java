package com.ruyuan.process.engine.config;

import com.ruyuan.process.engine.model.ProcessModel;

import java.util.List;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public interface ProcessParser {

    /**
     * 解析器
     *
     * @return 解析结果
     */
    List<ProcessModel> parse() throws Exception;

}

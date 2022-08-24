package com.ruyuan.process.engine;

import com.ruyuan.process.engine.config.ProcessParser;
import com.ruyuan.process.engine.model.ProcessContextFactory;
import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.store.ProcessStateStore;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程引擎启动器
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
public class ProcessEngine {

    private final ProcessContextFactory factory;

    public ProcessEngine(ProcessParser processParser) throws Exception {
        this(processParser, null);
    }

    public ProcessEngine(ProcessParser processParser, ProcessStateStore processStateStore) throws Exception {
        this.factory = new ProcessContextFactory(processParser.parse(), processStateStore);
    }

    /**
     * 获取一个流程上下文
     *
     * @param name 流程名称
     * @return 流程上下文
     */
    public ProcessContext getContext(String name) {
        return factory.getContext(name);
    }
}

package moe.ahao.process.engine.wrapper;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.process.ProcessContext;
import moe.ahao.process.engine.core.store.ProcessStateStore;
import moe.ahao.process.engine.wrapper.model.ProcessContextFactory;
import moe.ahao.process.engine.wrapper.model.ProcessModel;
import moe.ahao.process.engine.wrapper.parse.ProcessParser;

import java.util.List;

/**
 * 流程引擎启动器
 */
@Slf4j
public class ProcessEngine {
    private final ProcessContextFactory factory;

    public ProcessEngine(ProcessParser processParser) throws Exception {
        this(processParser, null);
    }

    public ProcessEngine(ProcessParser processParser, ProcessStateStore processStateStore) throws Exception {
        List<ProcessModel> processProperties = processParser.parse();
        this.factory = new ProcessContextFactory(processProperties, processStateStore);
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

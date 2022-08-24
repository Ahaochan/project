package moe.ahao.process.engine.wrapper.parse;

import moe.ahao.process.engine.wrapper.model.ProcessModel;

import java.util.List;

/**
 * 流程配置解析器
 */
public interface ProcessParser {
    /**
     * 解析器
     *
     * @return 解析结果
     */
    List<ProcessModel> parse() throws Exception;
}

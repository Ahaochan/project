package moe.ahao.process.engine.wrapper.parse;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.enums.InvokeMethod;
import moe.ahao.process.engine.wrapper.model.ProcessModel;
import moe.ahao.process.engine.wrapper.model.ProcessNodeModel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * XML格式的流程配置解析器
 * 使用dom4j进行解析
 */
@Slf4j
public abstract class XmlProcessParser implements ProcessParser {

    @Override
    public List<ProcessModel> parse() throws Exception {
        // 1. 交由子类, 获取Document文档对象
        Document document = this.getDocument();

        // 2. 获取根节点<process-context>
        Element rootElement = document.getRootElement();
        // 3. 获取每个流程<process>
        List<Element> processElements = rootElement.elements();
        List<ProcessModel> processModels = new ArrayList<>();
        for (Element processElement : processElements) {
            // 4. 解析流程<process>, 封装为ProcessModel对象
            String processName = processElement.attributeValue("name");
            log.debug("加载流程配置, 加载<process>标签, name:{}", processName);
            ProcessModel processModel = new ProcessModel(processName);
            // 5. 解析流程节点<node>
            List<Element> elements = processElement.elements();
            for (Element node : elements) {
                ProcessNodeModel processNodeModel = this.parseProcessNodeModel(node);
                processModel.addNode(processNodeModel);
            }
            log.debug("加载流程配置, 加载<process>标签成功, node节点{}个", elements.size());
            processModels.add(processModel);
        }
        return processModels;
    }

    protected abstract Document getDocument() throws DocumentException;

    private ProcessNodeModel parseProcessNodeModel(Element e) {
        String name = e.attributeValue("name");
        String className = e.attributeValue("class");
        String next = e.attributeValue("next");
        String begin = e.attributeValue("begin");
        String invokeMethodStr = e.attributeValue("invoke-method");
        log.debug("加载流程配置, 加载<node>标签, name:{}, class:{}, next:{}, begin:{}, invoke-method:{}", name, className, next, begin, invokeMethodStr);

        ProcessNodeModel node = new ProcessNodeModel(name, className);
        if (next != null) {
            node.setNextNodeNames(next);
        }
        node.setBegin(Boolean.parseBoolean(begin));
        InvokeMethod invokeMethod = invokeMethodStr == null ? InvokeMethod.SYNC : InvokeMethod.valueOf(invokeMethodStr.toUpperCase());
        node.setInvokeMethod(invokeMethod);
        return node;
    }

}

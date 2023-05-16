package moe.ahao.process.engine.wrapper.parse;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.enums.InvokeMethod;
import moe.ahao.process.engine.wrapper.constants.ProcessConstants;
import moe.ahao.process.engine.wrapper.model.BizConfigModel;
import moe.ahao.process.engine.wrapper.model.ProcessModel;
import moe.ahao.process.engine.wrapper.model.ProcessNodeModel;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * XML格式的流程配置解析器
 * 使用dom4j进行解析
 */
@Slf4j
public abstract class XmlProcessParser implements ProcessParser {

    @Override
    public List<ProcessModel> parse() {
        try {
            // 1. 交由子类, 获取Document文档对象
            Document document = this.getDocument();

            // 2. 获取根节点<process-context>
            Element rootElement = document.getRootElement();
            // 3. 获取每个流程<process>
            List<Element> processElements = rootElement.elements();
            List<ProcessModel> processModels = new ArrayList<>();
            for (Element processElement : processElements) {
                // 4. 解析流程<process>, 封装为ProcessModel对象
                String processName = processElement.attributeValue(ProcessConstants.NAME_ATTR);
                log.debug("加载流程配置, 加载<process>标签, name:{}", processName);
                ProcessModel processModel = new ProcessModel(processName);
                // 5. 解析流程节点<nodes>和<bizRelations>
                List<Element> nodesElements = processElement.elements();
                int nodesElementCount = 0;
                int bizRelationsElementCount = 0;
                for (Element nodeElement : nodesElements) {
                    // 5.1. 解析流程节点<nodes>
                    if(ProcessConstants.NODES_ELEMENT.equals(nodeElement.getName())) {
                        List<ProcessNodeModel> processNodeModel = this.processNodesElement(nodeElement);
                        processModel.addNode(processNodeModel);
                        nodesElementCount++;
                    }
                    // 5.2. 解析业务关联节点<bizRelations>
                    else if(ProcessConstants.BIZ_RELATIONS_ELEMENT.equals(nodeElement.getName())) {
                        List<BizConfigModel> bizConfigModels = this.processBizRelationsElement(nodeElement);
                        processModel.addBizConfig(bizConfigModels);
                        bizRelationsElementCount++;
                    }
                }
                processModels.add(processModel);
                log.debug("加载流程{}配置成功, 加载<nodes>标签{}个, 加载<bizRelations>标签{}个", processName, nodesElementCount, bizRelationsElementCount);
            }
            log.debug("加载流程配置成功, 加载<process>标签{}个", processElements.size());
            return processModels;
        } catch (Exception e) {
            log.error("加载流程配置失败", e);
            return Collections.emptyList();
        }
    }

    private List<ProcessNodeModel> processNodesElement(Element nodesElement) {
        List<Element> nodeElements = nodesElement.elements();
        List<ProcessNodeModel> processNodeModels = new ArrayList<>(nodeElements.size());
        for (Element e : nodeElements) {
            String name = e.attributeValue(ProcessConstants.NAME_ATTR);
            String className = e.attributeValue(ProcessConstants.CLAZZ_ATTR);
            String next = e.attributeValue(ProcessConstants.NEXT_ATTR);
            String begin = e.attributeValue(ProcessConstants.BEGIN_ATTR);
            String invokeMethodStr = e.attributeValue(ProcessConstants.INVOKE_METHOD_ATTR);
            log.debug("加载流程配置, 加载<node>标签, name:{}, class:{}, next:{}, begin:{}, invoke-method:{}", name, className, next, begin, invokeMethodStr);

            ProcessNodeModel processNodeModel = new ProcessNodeModel(name, className);
            if (next != null) {
                processNodeModel.setNextNodeNames(next);
            }
            processNodeModel.setBegin(Boolean.parseBoolean(begin));
            InvokeMethod invokeMethod = invokeMethodStr == null ? InvokeMethod.SYNC : InvokeMethod.valueOf(invokeMethodStr.toUpperCase());
            processNodeModel.setInvokeMethod(invokeMethod);

            processNodeModels.add(processNodeModel);
        }

        log.debug("加载流程配置成功, 加载<node>标签{}个", processNodeModels.size());
        return processNodeModels;
    }

    private List<BizConfigModel> processBizRelationsElement(Element bizRelationsElement) {
        List<Element> bizConfigElements = bizRelationsElement.elements();
        List<BizConfigModel> bizConfigModels = new ArrayList<>(bizConfigElements.size());
        for (Element e : bizConfigElements) {
            String name = e.attributeValue(ProcessConstants.NAME_ATTR);
            Integer businessIdentifier = Integer.valueOf(e.attributeValue(ProcessConstants.BIZ_ID_ATTR));
            Integer orderType = Integer.valueOf(e.attributeValue(ProcessConstants.ORDER_TYPE_ATTR));
            log.debug("加载流程配置, 加载<bizConfig>标签, name:{}, businessIdentifier:{}, orderType:{}", name, businessIdentifier, orderType);

            BizConfigModel bizConfigModel = new BizConfigModel();
            bizConfigModel.setName(name);
            bizConfigModel.setBusinessIdentifier(businessIdentifier);
            bizConfigModel.setOrderType(orderType);

            bizConfigModels.add(bizConfigModel);
        }
        return bizConfigModels;
    }

    protected abstract Document getDocument() throws Exception;
}

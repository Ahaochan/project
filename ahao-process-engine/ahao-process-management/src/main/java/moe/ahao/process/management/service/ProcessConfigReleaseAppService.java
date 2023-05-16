package moe.ahao.process.management.service;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.wrapper.constants.ProcessConstants;
import moe.ahao.process.management.controller.dto.ProcessConfigListDTO;
import moe.ahao.process.management.enums.ProcessEnableEnum;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 流程配置service
 */
@Slf4j
@Service
public class ProcessConfigReleaseAppService {
    /**
     * 订单流程配置在nacos上的dataId
     */
    public static final String ORDER_PROCESS_XML_DATA_ID = "ahao-order-process.xml";

    /**
     * 订单流程配置nacos默认group
     */
    public static final String ORDER_PROCESS_DEFAULT_GROUP = "DEFAULT_GROUP";

    @Autowired
    private ProcessConfigQueryService processConfigQueryService;
    @Autowired
    private ConfigService nacosClient;

    /**
     * 发布流程
     */
    public void release() {
        // 1. 查询所有启用的流程配置
        List<ProcessConfigListDTO> processConfigs = processConfigQueryService.list(Arrays.asList(ProcessEnableEnum.ENABLE.getCode()));

        // 2. 生成流程配置xml Document
        Document document = this.buildProcessDocument(processConfigs);

        // 3. 读取xml文件内容
        String processXml = document.asXML();

        // 4. 发布流程配置到nacos
        if (StringUtils.isNotBlank(processXml)) {
            this.publishConfig(processXml);
        }
    }

    private Document buildProcessDocument(List<ProcessConfigListDTO> processConfigList) {
        // 1. 初始化根节点
        Document document = DocumentHelper.createDocument();
        Element processContext = document.addElement(ProcessConstants.PROCESS_CONTEXT_ELEMENT, "http://www.w3school.com.cn");
        processContext.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        processContext.addAttribute("xsi:schemaLocation", "http://www.w3school.com.cn process-engine.xsd");

        // 2. 初始化<process>标签
        for (ProcessConfigListDTO processConfig : processConfigList) {
            Element process = processContext.addElement(ProcessConstants.PROCESS_ELEMENT);
            process.addAttribute(ProcessConstants.NAME_ATTR, processConfig.getXmlName());

            // 3. 初始化<nodes>标签
            Element nodes = process.addElement(ProcessConstants.NODES_ELEMENT);
            List<ProcessConfigListDTO.ProcessNodeLinkedDTO> nodeLinkedList = processConfig.getProcessNodeLinked();
            int n = nodeLinkedList.size();
            for (int i = 0; i < n; i++) {
                // 3. 初始化<node>标签
                ProcessConfigListDTO.ProcessNodeLinkedDTO nodeLinked = nodeLinkedList.get(i);
                Element node = nodes.addElement(ProcessConstants.NODE_ELEMENT);
                node.addAttribute(ProcessConstants.NAME_ATTR, nodeLinked.getProcessNodeBeanName());
                node.addAttribute(ProcessConstants.CLAZZ_ATTR, nodeLinked.getProcessNodeBeanClazzName());
                node.addAttribute(ProcessConstants.INVOKE_METHOD_ATTR, nodeLinked.getInvokeMethod().toLowerCase(Locale.ROOT));
                if (i + 1 < n) {
                    ProcessConfigListDTO.ProcessNodeLinkedDTO nextNodeLinked = nodeLinkedList.get(i + 1);
                    node.addAttribute(ProcessConstants.NEXT_ATTR, nextNodeLinked.getProcessNodeBeanName());
                }
                if (i == 0) {
                    node.addAttribute(ProcessConstants.BEGIN_ATTR, Boolean.TRUE.toString());
                }
            }

            // 增加bizRelations
            Element bizRelations = process.addElement(ProcessConstants.BIZ_RELATIONS_ELEMENT);
            for (ProcessConfigListDTO.BizConfigListDTO configListDTO : processConfig.getBizConfigRelations()) {
                Element bizConfig = bizRelations.addElement(ProcessConstants.BIZ_CONFIG_ELEMENT);
                bizConfig.addAttribute(ProcessConstants.NAME_ATTR, configListDTO.getName());
                bizConfig.addAttribute(ProcessConstants.BIZ_ID_ATTR, String.valueOf(configListDTO.getBusinessIdentifier()));
                bizConfig.addAttribute(ProcessConstants.ORDER_TYPE_ATTR, String.valueOf(configListDTO.getOrderType()));
            }
        }
        return document;
    }

    /**
     * 发布配置
     */
    private void publishConfig(String processXml) {
        try {
            String group = ORDER_PROCESS_DEFAULT_GROUP;
            String dataId = ORDER_PROCESS_XML_DATA_ID;

            log.info("发布流程配置到nacos上去, processXml:{}, dataId:{}, group:{}", processXml, dataId, group);
            nacosClient.publishConfig(dataId, group, processXml, ConfigType.XML.getType());
        } catch (Exception e) {
            log.error("发布流程配置异常, err:{}", e.getMessage(), e);
        }
    }
}

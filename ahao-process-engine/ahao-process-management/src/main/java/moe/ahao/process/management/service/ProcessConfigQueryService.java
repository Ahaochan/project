package moe.ahao.process.management.service;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.management.controller.dto.ProcessConfigDetailDTO;
import moe.ahao.process.management.controller.dto.ProcessConfigListDTO;
import moe.ahao.process.management.exception.ProcessExceptionEnum;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.*;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 流程配置service
 */
@Slf4j
@Service
public class ProcessConfigQueryService {
    @Autowired
    private ProcessConfigMapper processConfigMapper;
    @Autowired
    private ProcessNodeLinkedMapper processNodeLinkedMapper;
    @Autowired
    private ProcessBizRelationMapper processBizRelationMapper;
    @Autowired
    private ProcessNodeMapper processNodeMapper;
    @Autowired
    private ProcessBizConfigMapper processBizConfigMapper;

    /**
     * 查询流程配置详情
     *
     * @param name 流程节点名称
     */
    public ProcessConfigDetailDTO detail(String name) {
        if (StringUtils.isEmpty(name)) {
            throw ProcessExceptionEnum.PROCESS_NAME_IS_EMPTY.msg();
        }

        // 1. 根据名称查询流程配置
        ProcessConfigDO processConfig = processConfigMapper.selectOneByName(name);
        if (processConfig == null) {
            return null;
        }
        // 2. 构造返参
        ProcessConfigDetailDTO result = new ProcessConfigDetailDTO();
        result.setId(processConfig.getId());
        result.setType(processConfig.getType());
        result.setXmlName(processConfig.getXmlName());
        result.setName(processConfig.getName());
        result.setRemark(processConfig.getRemark());
        result.setEnable(processConfig.getEnable());

        // 3. 查询关联的流程构建和业务关联关系
        // 拼数据
        List<ProcessNodeLinkedDO> processNodeLinkedList = processNodeLinkedMapper.selectListByProcessConfigName(name);
        Map<String, ProcessNodeDO> processNodeMap = this.getProcessNodeMap(processNodeLinkedList);

        List<ProcessConfigDetailDTO.ProcessNodeLinkedDTO> processNodeLinkedDTOList = new ArrayList<>();
        for (ProcessNodeLinkedDO linkedDO : processNodeLinkedList) {
            ProcessNodeDO processNode = processNodeMap.get(linkedDO.getProcessNodeName());

            ProcessConfigDetailDTO.ProcessNodeLinkedDTO dto = new ProcessConfigDetailDTO.ProcessNodeLinkedDTO();
            dto.setProcessNodeBeanName(processNode.getBeanName());
            dto.setProcessNodeBeanClazzName(processNode.getBeanClazzName());
            dto.setProcessNodeName(linkedDO.getProcessNodeName());
            dto.setInvokeMethod(linkedDO.getInvokeMethod());

            processNodeLinkedDTOList.add(dto);
        }
        result.setProcessNodeLinked(processNodeLinkedDTOList);

        // 拼数据
        List<ProcessBizRelationDO> processBizRelationList = processBizRelationMapper.selectListByProcessConfigName(name);
        Map<Long, ProcessBizConfigDO> processBizConfigMap = this.getProcessBizConfigMap(processBizRelationList);
        List<ProcessConfigDetailDTO.BizConfigListDTO> bizConfigDTOList = new ArrayList<>();
        for (ProcessBizRelationDO relationDO : processBizRelationList) {
            ProcessBizConfigDO bizConfigDO = processBizConfigMap.get(relationDO.getProcessBizConfigId());

            ProcessConfigDetailDTO.BizConfigListDTO bizConfigDTO = new ProcessConfigDetailDTO.BizConfigListDTO();
            bizConfigDTO.setId(bizConfigDO.getId());
            bizConfigDTO.setName(bizConfigDO.getName());
            bizConfigDTO.setBusinessIdentifier(bizConfigDO.getBusinessIdentifier());
            bizConfigDTO.setOrderType(bizConfigDO.getOrderType());

            bizConfigDTOList.add(bizConfigDTO);
        }
        result.setBizConfigRelations(bizConfigDTOList);
        return result;
    }

    /**
     * 流程节点列表展示
     */
    public List<ProcessConfigListDTO> list(List<Integer> enables) {
        // 1. 根据名称查询流程配置
        List<ProcessConfigDO> processConfigList = processConfigMapper.selectListByEnables(enables);
        if (CollectionUtils.isEmpty(processConfigList)) {
            return Collections.emptyList();
        }
        List<String> processConfigNameList = processConfigList.stream().map(ProcessConfigDO::getName).collect(Collectors.toList());

        // 2. 查询关联的流程构建和业务关联关系
        List<ProcessNodeLinkedDO> processNodeLinkedAllList = processNodeLinkedMapper.selectListByProcessConfigNames(processConfigNameList);
        Map<String, List<ProcessNodeLinkedDO>> processNodeLinkedMap = processNodeLinkedAllList.stream().collect(Collectors.groupingBy(ProcessNodeLinkedDO::getProcessConfigName));
        Map<String, ProcessNodeDO> processNodeMap = this.getProcessNodeMap(processNodeLinkedAllList);

        List<ProcessBizRelationDO> processBizRelationAllList = processBizRelationMapper.selectListByProcessConfigNames(processConfigNameList);
        Map<String, List<ProcessBizRelationDO>> processBizRelationMap = processBizRelationAllList.stream().collect(Collectors.groupingBy(ProcessBizRelationDO::getProcessConfigName));
        Map<Long, ProcessBizConfigDO> processBizConfigMap = this.getProcessBizConfigMap(processBizRelationAllList);

        // 3. 构造返回参数
        List<ProcessConfigListDTO> result = new ArrayList<>(processConfigList.size());
        for (ProcessConfigDO processConfig : processConfigList) {
            ProcessConfigListDTO processConfigListDTO = new ProcessConfigListDTO();
            processConfigListDTO.setId(processConfig.getId());
            processConfigListDTO.setType(processConfig.getType());
            processConfigListDTO.setXmlName(processConfig.getXmlName());
            processConfigListDTO.setName(processConfig.getName());
            processConfigListDTO.setRemark(processConfig.getRemark());
            processConfigListDTO.setEnable(processConfig.getEnable());

            List<ProcessNodeLinkedDO> processNodeLinkedList = processNodeLinkedMap.get(processConfig.getName());
            List<ProcessConfigListDTO.ProcessNodeLinkedDTO> processNodeLinkedDTOList = new ArrayList<>();
            for (ProcessNodeLinkedDO linkedDO : processNodeLinkedList) {
                ProcessNodeDO processNode = processNodeMap.get(linkedDO.getProcessNodeName());

                ProcessConfigListDTO.ProcessNodeLinkedDTO dto = new ProcessConfigListDTO.ProcessNodeLinkedDTO();
                dto.setProcessNodeBeanName(processNode.getBeanName());
                dto.setProcessNodeBeanClazzName(processNode.getBeanClazzName());
                dto.setProcessNodeName(linkedDO.getProcessNodeName());
                dto.setInvokeMethod(linkedDO.getInvokeMethod());


                processNodeLinkedDTOList.add(dto);
            }
            processConfigListDTO.setProcessNodeLinked(processNodeLinkedDTOList);

            List<ProcessBizRelationDO> processBizRelation = processBizRelationMap.get(processConfig.getName());
            List<ProcessConfigListDTO.BizConfigListDTO> bizConfigDTOList = new ArrayList<>();
            for (ProcessBizRelationDO relationDO : processBizRelation) {
                ProcessBizConfigDO bizConfigDO = processBizConfigMap.get(relationDO.getProcessBizConfigId());

                ProcessConfigListDTO.BizConfigListDTO bizConfigDTO = new ProcessConfigListDTO.BizConfigListDTO();
                bizConfigDTO.setId(bizConfigDO.getId());
                bizConfigDTO.setName(bizConfigDO.getName());
                bizConfigDTO.setBusinessIdentifier(bizConfigDO.getBusinessIdentifier());
                bizConfigDTO.setOrderType(bizConfigDO.getOrderType());

                bizConfigDTOList.add(bizConfigDTO);
            }
            processConfigListDTO.setBizConfigRelations(bizConfigDTOList);


            result.add(processConfigListDTO);
        }

        return result;
    }

    /**
     * 根据流程业务关联关系，获取关联的流程业务配置
     *
     * @param list 流程业务关联关系
     * @return 以流程业务配置id为key，流程业务配置为value的Map集合
     */
    private Map<Long, ProcessBizConfigDO> getProcessBizConfigMap(List<ProcessBizRelationDO> list) {
        List<Long> processBizConfigIdList = list.stream().map(ProcessBizRelationDO::getProcessBizConfigId).distinct().collect(Collectors.toList());
        List<ProcessBizConfigDO> processBizConfigList = processBizConfigMapper.selectListInIds(processBizConfigIdList);
        Map<Long, ProcessBizConfigDO> map = processBizConfigList.stream().collect(Collectors.toMap(ProcessBizConfigDO::getId, Function.identity()));
        return map;
    }

    /**
     * 根据流程节点关联关系，获取关联的流程业务配置
     *
     * @param list 流程节点关联关系
     * @return 以流程节点名称为key，流程节点为value的Map集合
     */
    private Map<String, ProcessNodeDO> getProcessNodeMap(List<ProcessNodeLinkedDO> list) {
        List<String> processNodeNameList = list.stream().map(ProcessNodeLinkedDO::getProcessNodeName).collect(Collectors.toList());
        List<ProcessNodeDO> processNodeList = processNodeMapper.selectListInName(processNodeNameList);
        Map<String, ProcessNodeDO> map = processNodeList.stream().collect(Collectors.toMap(ProcessNodeDO::getName, Function.identity()));
        return map;
    }
}

package moe.ahao.process.management.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import moe.ahao.exception.CommonBizExceptionEnum;
import moe.ahao.process.management.controller.dto.BizConfigListDTO;
import moe.ahao.process.management.controller.dto.ProcessConfigSaveCommand;
import moe.ahao.process.management.controller.dto.ProcessNodeLinkedDTO;
import moe.ahao.process.management.enums.InvokeMethodEnum;
import moe.ahao.process.management.enums.ProcessEnableEnum;
import moe.ahao.process.management.enums.ProcessTypeEnum;
import moe.ahao.process.management.exception.ProcessExceptionEnum;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessBizRelationDO;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessConfigDO;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessNodeDO;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessNodeLinkedDO;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessBizRelationMapper;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessConfigMapper;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessNodeLinkedMapper;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessNodeMapper;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.service.ProcessBizRelationMybatisService;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.service.ProcessNodeLinkedMybatisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程配置service
 */
@Slf4j
@Service
public class ProcessConfigSaveAppService {
    @Autowired
    private ProcessNodeLinkedMybatisService processNodeLinkedMybatisService;
    @Autowired
    private ProcessBizRelationMybatisService processBizRelationMybatisService;

    @Autowired
    private ProcessConfigMapper processConfigMapper;
    @Autowired
    private ProcessNodeMapper processNodeMapper;
    @Autowired
    private ProcessNodeLinkedMapper processNodeLinkedMapper;
    @Autowired
    private ProcessBizRelationMapper processBizRelationMapper;

    /**
     * 保存流程配置
     *
     * @param command 请求
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(ProcessConfigSaveCommand command) {
        // 1. 入参校验
        this.check(command);

        // 2. 根据名称查询流程配置
        ProcessConfigDO processConfig = processConfigMapper.selectOneByName(command.getName());
        boolean insert = (processConfig == null);
        if (insert) {
            processConfig = new ProcessConfigDO();
        }

        // 3. 设置属性
        processConfig.setType(command.getType());
        processConfig.setXmlName(command.getXmlName());
        processConfig.setName(command.getName());
        processConfig.setRemark(command.getRemark());
        processConfig.setEnable(ProcessEnableEnum.ENABLE.getCode());

        // 3. 流程构建
        List<ProcessNodeLinkedDO> processNodeLinkedList = this.buildProcessNodeLinkedDO(command.getName(), command.getProcessNodeLinked());

        // 4. 业务关联关系
        List<ProcessBizRelationDO> processBizRelationList = this.buildProcessBizRelationDO(command.getName(), command.getBizConfigRelations());

        // 5、清除之前的流程构建和业务关联关系
        processNodeLinkedMapper.deleteByProcessConfigName(command.getName());
        processBizRelationMapper.deleteByProcessConfigName(command.getName());

        // 6、重新插入流程构建和业务关联关系
        processNodeLinkedMybatisService.saveBatch(processNodeLinkedList);
        processBizRelationMybatisService.saveBatch(processBizRelationList);

        // 7、插入流程
        if (insert) {
            processConfigMapper.insert(processConfig);
        } else {
            processConfigMapper.updateById(processConfig);
        }
    }

    private List<ProcessNodeLinkedDO> buildProcessNodeLinkedDO(String processConfigName, List<ProcessNodeLinkedDTO> processNodeLinked) {
        List<ProcessNodeLinkedDO> list = new ArrayList<>();
        for (ProcessNodeLinkedDTO node : processNodeLinked) {
            ProcessNodeDO nodeDO = processNodeMapper.selectById(node.getProcessNodeId());
            if (null == nodeDO) {
                throw ProcessExceptionEnum.PROCESS_NODE_NOT_FOUND.msg();
            }
            if (!node.getProcessNodeName().equals(nodeDO.getName())) {
                throw ProcessExceptionEnum.PROCESS_NODE_NAME_NOT_MATCH.msg();
            }

            ProcessNodeLinkedDO processNodeLinkedDO = new ProcessNodeLinkedDO();
            processNodeLinkedDO.setProcessNodeName(node.getProcessNodeName());
            processNodeLinkedDO.setProcessConfigName(processConfigName);
            processNodeLinkedDO.setInvokeMethod(processNodeLinkedDO.getInvokeMethod());
            list.add(processNodeLinkedDO);
        }

        return list;
    }

    private List<ProcessBizRelationDO> buildProcessBizRelationDO(String processConfigName, List<BizConfigListDTO> bizConfigList) {
        List<ProcessBizRelationDO> list = new ArrayList<>();
        for (BizConfigListDTO biz : bizConfigList) {
            ProcessBizRelationDO relationDO = new ProcessBizRelationDO();
            relationDO.setProcessBizConfigId(biz.getId());
            relationDO.setProcessConfigName(processConfigName);
            list.add(relationDO);
        }
        return list;
    }

    /**
     * 校验入参
     *
     * @param command 入参
     */
    private void check(ProcessConfigSaveCommand command) {
        if (!ProcessTypeEnum.allowableValues().contains(command.getType())) {
            throw CommonBizExceptionEnum.ENUM_PARAM_MUST_BE_IN_ALLOWABLE_VALUE.msg("type", ProcessTypeEnum.allowableValues());
        }
        if (StringUtils.isBlank(command.getName())) {
            throw ProcessExceptionEnum.PROCESS_NAME_IS_EMPTY.msg();
        }
        if (StringUtils.isBlank(command.getXmlName())) {
            throw ProcessExceptionEnum.PROCESS_XML_NAME_IS_EMPTY.msg();
        }
        if (CollectionUtils.isEmpty(command.getProcessNodeLinked())) {
            throw ProcessExceptionEnum.PROCESS_NODE_LINKED_IS_EMPTY.msg();
        }
        if (CollectionUtils.isEmpty(command.getBizConfigRelations())) {
            throw ProcessExceptionEnum.BIZ_CONFIG_RELATION_IS_EMPTY.msg();
        }

        for (ProcessNodeLinkedDTO nodeLinkedDTO : command.getProcessNodeLinked()) {
            if (StringUtils.isBlank(nodeLinkedDTO.getProcessNodeName())) {
                throw ProcessExceptionEnum.PROCESS_NODE_NAME_IS_EMPTY.msg();
            }
            if (nodeLinkedDTO.getProcessNodeId() == null) {
                throw ProcessExceptionEnum.PROCESS_NODE_ID_IS_EMPTY.msg();
            }
            InvokeMethodEnum invokeMethodEnum = InvokeMethodEnum.getByCode(nodeLinkedDTO.getInvokeMethod());
            if (invokeMethodEnum == null) {
                throw ProcessExceptionEnum.INVOKE_METHOD_IS_ERROR.msg();
            }
        }
    }
}

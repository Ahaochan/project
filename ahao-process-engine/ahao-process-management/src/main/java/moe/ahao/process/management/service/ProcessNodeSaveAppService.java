package moe.ahao.process.management.service;

import moe.ahao.exception.CommonBizExceptionEnum;
import moe.ahao.process.management.controller.dto.SaveProcessNodeCommand;
import moe.ahao.process.management.enums.ProcessTypeEnum;
import moe.ahao.process.management.exception.ProcessExceptionEnum;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessNodeDO;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessNodeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ProcessNodeSaveAppService {
    @Autowired
    private ProcessNodeMapper processNodeMapper;

    @Transactional(rollbackFor = Exception.class)
    public void save(SaveProcessNodeCommand command) {
        this.check(command);

        ProcessNodeDO processNode = processNodeMapper.selectOneByName(command.getName());
        boolean insert = (processNode == null);
        if (insert) {
            processNode = new ProcessNodeDO();
        }
        processNode.setType(command.getType());
        processNode.setName(command.getName());
        processNode.setBeanName(command.getBeanName());
        processNode.setBeanClazzName(command.getBeanClazzName());
        processNode.setRemark(command.getRemark());

        // 3、保存
        if (insert) {
            processNodeMapper.insert(processNode);
        } else {
            processNodeMapper.updateById(processNode);
        }
    }

    private void check(SaveProcessNodeCommand command) {
        Set<Integer> processTypeAllowableValues = ProcessTypeEnum.allowableValues();
        if (!processTypeAllowableValues.contains(command.getType())) {
            throw CommonBizExceptionEnum.ENUM_PARAM_MUST_BE_IN_ALLOWABLE_VALUE.msg("type", processTypeAllowableValues);
        }
        if (StringUtils.isEmpty(command.getName())) {
            throw ProcessExceptionEnum.PROCESS_NODE_NAME_IS_EMPTY.msg();
        }
        if (StringUtils.isEmpty(command.getBeanName())) {
            throw ProcessExceptionEnum.PROCESS_NODE_BEAN_NAME_IS_EMPTY.msg();
        }
        if (StringUtils.isEmpty(command.getBeanClazzName())) {
            throw ProcessExceptionEnum.PROCESS_NODE_BEAN_CLAZZ_NAME_IS_EMPTY.msg();
        }
    }
}

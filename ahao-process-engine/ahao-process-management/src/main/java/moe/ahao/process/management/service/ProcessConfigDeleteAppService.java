package moe.ahao.process.management.service;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessConfigDO;
import moe.ahao.process.management.controller.dto.ProcessConfigDeleteCommand;
import moe.ahao.process.management.exception.ProcessExceptionEnum;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessBizRelationMapper;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessConfigMapper;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessNodeLinkedMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 流程配置service
 */
@Slf4j
@Service
public class ProcessConfigDeleteAppService {
    @Autowired
    private ProcessConfigMapper processConfigMapper;
    @Autowired
    private ProcessNodeLinkedMapper processNodeLinkedMapper;
    @Autowired
    private ProcessBizRelationMapper processBizRelationMapper;

    /**
     * 删除流程
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(ProcessConfigDeleteCommand command) {
        // 1. 校验入参
        this.check(command);

        // 2. 根据名称查询流程配置
        String name = command.getName();
        ProcessConfigDO processConfig = processConfigMapper.selectOneByName(name);
        if (processConfig == null) {
            return;
        }

        // 3. 清除关联的流程构建和业务关联关系
        processNodeLinkedMapper.deleteByProcessConfigName(name);
        processBizRelationMapper.deleteByProcessConfigName(name);

        // 4. 移除自身
        processConfigMapper.deleteByName(name);
    }

    private void check(ProcessConfigDeleteCommand command) {
        if (StringUtils.isEmpty(command.getName())) {
            throw ProcessExceptionEnum.PROCESS_NAME_IS_EMPTY.msg();
        }
    }
}

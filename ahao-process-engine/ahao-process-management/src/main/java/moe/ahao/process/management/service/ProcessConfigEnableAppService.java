package moe.ahao.process.management.service;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.management.controller.dto.ProcessConfigEnableCommand;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessConfigDO;
import moe.ahao.process.management.enums.ProcessEnableEnum;
import moe.ahao.process.management.exception.ProcessExceptionEnum;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 流程配置service
 */
@Slf4j
@Service
public class ProcessConfigEnableAppService {
    @Autowired
    private ProcessConfigMapper processConfigMapper;

    /**
     * 启用/禁用
     */
    public void enable(ProcessConfigEnableCommand command) {
        // 1、校验入参
        this.check(command);

        // 2. 根据名称查询流程配置
        ProcessConfigDO processConfig = processConfigMapper.selectOneByName(command.getName());
        if (processConfig == null) {
            return;
        }
        // 3. 启用/禁用
        processConfigMapper.updateEnableByName(command.getName(), command.getEnable());
    }

    private void check(ProcessConfigEnableCommand command) {
        String name = command.getName();
        if (StringUtils.isEmpty(name)) {
            throw ProcessExceptionEnum.PROCESS_NAME_IS_EMPTY.msg();
        }

        if (!ProcessEnableEnum.allowableValues().contains(command.getEnable())) {
            throw ProcessExceptionEnum.PROCESS_ENABLE_IS_ERROR.msg();
        }
    }
}

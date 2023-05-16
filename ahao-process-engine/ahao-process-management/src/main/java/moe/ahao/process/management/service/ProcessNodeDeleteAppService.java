package moe.ahao.process.management.service;

import moe.ahao.process.management.controller.dto.DeleteProcessNodeCommand;
import moe.ahao.process.management.exception.ProcessExceptionEnum;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessNodeDO;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessNodeMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessNodeDeleteAppService {
    @Autowired
    private ProcessNodeMapper processNodeMapper;

    @Transactional(rollbackFor = Exception.class)
    public void delete(DeleteProcessNodeCommand command) {
        this.check(command);

        String name = command.getName();
        // 1、根据节点名称查询对应的流程节点
        ProcessNodeDO processNode = processNodeMapper.selectOneByName(name);
        if (processNode == null) {
            return;
        }

        // 2、移除
        processNodeMapper.deleteById(processNode.getId());
    }

    private void check(DeleteProcessNodeCommand command) {
        if (StringUtils.isEmpty(command.getName())) {
            throw ProcessExceptionEnum.PROCESS_NODE_NAME_IS_EMPTY.msg();
        }
    }
}

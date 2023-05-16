package moe.ahao.process.management.service;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.management.controller.dto.ProcessNodeDetailQuery;
import moe.ahao.process.management.controller.dto.ProcessNodeListQuery;
import moe.ahao.process.management.controller.dto.ProcessNodeDTO;
import moe.ahao.process.management.controller.dto.ProcessNodeListDTO;
import moe.ahao.process.management.exception.ProcessExceptionEnum;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessNodeDO;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessNodeMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProcessNodeQueryService {
    @Autowired
    private ProcessNodeMapper processNodeMapper;

    public ProcessNodeDTO query(ProcessNodeDetailQuery query) {
        String name = query.getName();
        if (StringUtils.isEmpty(name)) {
            throw ProcessExceptionEnum.PROCESS_NODE_NAME_IS_EMPTY.msg();
        }

        // 1、根据节点名称查询对应的流程节点
        ProcessNodeDO processNode = processNodeMapper.selectOneByName(name);
        if (null == processNode) {
            return null;
        }
        // 2、转化
        ProcessNodeDTO dto = new ProcessNodeDTO();
        dto.setId(processNode.getId());
        dto.setType(processNode.getType());
        dto.setName(processNode.getName());
        dto.setBeanName(processNode.getBeanName());
        dto.setBeanClazzName(processNode.getBeanClazzName());
        dto.setRemark(processNode.getRemark());
        return dto;
    }

    public List<ProcessNodeListDTO> query(ProcessNodeListQuery query) {
        // 1、根据条件查询流程节点
        List<ProcessNodeDO> list = processNodeMapper.selectListByTypeLikeName(query.getType(), query.getName());
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        // 2、组装查询条件
        List<ProcessNodeListDTO> result = list.stream()
            .map(processNodeDO -> new ProcessNodeListDTO(processNodeDO.getId(), processNodeDO.getType(), processNodeDO.getName()))
            .collect(Collectors.toList());
        return result;
    }
}

package moe.ahao.process.management.service;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.management.controller.dto.BizConfigListQuery;
import moe.ahao.process.management.controller.dto.BizConfigListDTO;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessBizConfigDO;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessBizConfigMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务配置service
 */
@Slf4j
@Service
public class BizConfigQueryService {

    @Autowired
    private ProcessBizConfigMapper bizConfigMapper;

    /**
     * 业务配置列表查询
     *
     * @return 响应
     */
    public List<BizConfigListDTO> list(BizConfigListQuery query) {
        // 1. 根据条件查询流程节点
        List<ProcessBizConfigDO> list = bizConfigMapper.selectListLikeName(query.getName());
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        // 2. 组装查询条件
        List<BizConfigListDTO> result = list.stream()
            .map(data -> new BizConfigListDTO(data.getId(), data.getName()))
            .collect(Collectors.toList());
        return result;
    }
}

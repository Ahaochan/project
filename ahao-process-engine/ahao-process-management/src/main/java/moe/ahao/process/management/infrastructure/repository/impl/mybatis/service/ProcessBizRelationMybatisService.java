package moe.ahao.process.management.infrastructure.repository.impl.mybatis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessBizRelationDO;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessBizRelationMapper;
import org.springframework.stereotype.Repository;

/**
 * 流程业务关联表 DAO
 */
@Repository
public class ProcessBizRelationMybatisService extends ServiceImpl<ProcessBizRelationMapper, ProcessBizRelationDO> {
}

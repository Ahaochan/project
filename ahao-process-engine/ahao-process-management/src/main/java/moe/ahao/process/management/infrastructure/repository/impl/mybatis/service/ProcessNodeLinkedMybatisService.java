package moe.ahao.process.management.infrastructure.repository.impl.mybatis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessNodeLinkedDO;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper.ProcessNodeLinkedMapper;
import org.springframework.stereotype.Repository;

/**
 * 流程节点表 DAO
 */
@Repository
public class ProcessNodeLinkedMybatisService extends ServiceImpl<ProcessNodeLinkedMapper, ProcessNodeLinkedDO> {
}

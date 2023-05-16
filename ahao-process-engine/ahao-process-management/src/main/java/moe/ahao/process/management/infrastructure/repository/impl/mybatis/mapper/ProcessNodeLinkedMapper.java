package moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessNodeLinkedDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程节点构建表 Mapper 接口
 */
@Mapper
public interface ProcessNodeLinkedMapper extends BaseMapper<ProcessNodeLinkedDO> {
    int deleteByProcessConfigName(@Param("name") String name);

    List<ProcessNodeLinkedDO> selectListByProcessConfigName(@Param("processConfigName") String processConfigName);
    List<ProcessNodeLinkedDO> selectListByProcessConfigNames(@Param("processConfigNames") List<String> processConfigNames);
}

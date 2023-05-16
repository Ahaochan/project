package moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessBizRelationDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程业务关联表 Mapper 接口
 */
@Mapper
public interface ProcessBizRelationMapper extends BaseMapper<ProcessBizRelationDO> {
    int deleteByProcessConfigName(@Param("name") String name);

    List<ProcessBizRelationDO> selectListByProcessConfigName(@Param("processConfigName") String processConfigName);
    List<ProcessBizRelationDO> selectListByProcessConfigNames(@Param("processConfigNames") List<String> processConfigNames);
}

package moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessNodeDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程节点表 Mapper 接口
 */
@Mapper
public interface ProcessNodeMapper extends BaseMapper<ProcessNodeDO> {
    ProcessNodeDO selectOneByName(@Param("name") String name);

    List<ProcessNodeDO> selectListInName(@Param("names") List<String> names);
    List<ProcessNodeDO> selectListByTypeLikeName(@Param("type") Integer type, @Param("name") String name);
}

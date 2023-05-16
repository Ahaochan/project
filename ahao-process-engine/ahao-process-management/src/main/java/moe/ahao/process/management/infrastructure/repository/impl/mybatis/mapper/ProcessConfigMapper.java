package moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程配置表 Mapper 接口
 */
@Mapper
public interface ProcessConfigMapper extends BaseMapper<ProcessConfigDO> {
    int updateEnableByName(@Param("name") String name, @Param("enable") Integer enable);
    int deleteByName(@Param("name") String name);

    ProcessConfigDO selectOneByName(@Param("name") String name);
    List<ProcessConfigDO> selectListByEnables(@Param("enables") List<Integer> enables);
}

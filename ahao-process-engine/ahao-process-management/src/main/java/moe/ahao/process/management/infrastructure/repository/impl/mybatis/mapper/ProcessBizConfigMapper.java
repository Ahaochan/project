package moe.ahao.process.management.infrastructure.repository.impl.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import moe.ahao.process.management.infrastructure.repository.impl.mybatis.data.ProcessBizConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务配置表 Mapper 接口
 */
@Mapper
public interface ProcessBizConfigMapper extends BaseMapper<ProcessBizConfigDO> {
    List<ProcessBizConfigDO> selectListLikeName(@Param("name") String name);
    List<ProcessBizConfigDO> selectListInIds(@Param("ids") List<Long> ids);
}

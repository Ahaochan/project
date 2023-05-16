package moe.ahao.process.management.controller;


import moe.ahao.domain.entity.Result;
import moe.ahao.process.management.controller.dto.BizConfigListDTO;
import moe.ahao.process.management.controller.dto.BizConfigListQuery;
import moe.ahao.process.management.service.BizConfigQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 业务配置 前端控制器
 */
@RestController
@RequestMapping("/biz-config")
public class BizConfigController {

    @Autowired
    private BizConfigQueryService bizConfigQueryService;

    /**
     * 业务配置列表查询
     *
     * @param query 查询请求
     * @return 响应
     */
    @PostMapping("/list")
    public Result<List<BizConfigListDTO>> list(@RequestBody BizConfigListQuery query) {
        List<BizConfigListDTO> list = bizConfigQueryService.list(query);
        return Result.success(list);
    }
}

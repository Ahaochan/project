package moe.ahao.process.management.controller;


import moe.ahao.domain.entity.Result;
import moe.ahao.process.management.controller.dto.DeleteProcessNodeCommand;
import moe.ahao.process.management.controller.dto.ProcessNodeDetailQuery;
import moe.ahao.process.management.controller.dto.ProcessNodeListQuery;
import moe.ahao.process.management.controller.dto.SaveProcessNodeCommand;
import moe.ahao.process.management.controller.dto.ProcessNodeDTO;
import moe.ahao.process.management.controller.dto.ProcessNodeListDTO;
import moe.ahao.process.management.service.ProcessNodeDeleteAppService;
import moe.ahao.process.management.service.ProcessNodeSaveAppService;
import moe.ahao.process.management.service.ProcessNodeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程节点 前端控制器
 */
@RestController
@RequestMapping("/process-node")
public class ProcessNodeController {
    @Autowired
    private ProcessNodeQueryService processNodeQueryService;
    @Autowired
    private ProcessNodeSaveAppService processNodeSaveAppService;
    @Autowired
    private ProcessNodeDeleteAppService processNodeDeleteAppService;

    /**
     * 保存流程节点
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping("/")
    public Result<Boolean> save(@RequestBody SaveProcessNodeCommand request) {
        processNodeSaveAppService.save(request);
        return Result.success(true);
    }

    /**
     * 删除流程节点
     *
     * @param request 请求
     * @return 响应
     */
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody DeleteProcessNodeCommand request) {
        processNodeDeleteAppService.delete(request);
        return Result.success(true);
    }

    /**
     * 流程节点详情
     *
     * @param name 流程节点名称
     * @return 响应
     */
    @GetMapping("/detail")
    public Result<ProcessNodeDTO> detail(String name) {
        ProcessNodeDetailQuery query = new ProcessNodeDetailQuery();
        query.setName(name);
        ProcessNodeDTO dto = processNodeQueryService.query(query);
        return Result.success(dto);
    }

    /**
     * 流程节点列表查询
     *
     * @param query 查询请求
     * @return 响应
     */
    @PostMapping("/list")
    public Result<List<ProcessNodeListDTO>> list(@RequestBody ProcessNodeListQuery query) {
        return Result.success(processNodeQueryService.query(query));
    }
}

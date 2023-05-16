package moe.ahao.process.management.controller;


import moe.ahao.domain.entity.Result;
import moe.ahao.process.management.controller.dto.ProcessConfigDetailDTO;
import moe.ahao.process.management.controller.dto.ProcessConfigEnableCommand;
import moe.ahao.process.management.controller.dto.ProcessConfigListDTO;
import moe.ahao.process.management.controller.dto.ProcessConfigDeleteCommand;
import moe.ahao.process.management.controller.dto.ProcessConfigSaveCommand;
import moe.ahao.process.management.enums.ProcessEnableEnum;
import moe.ahao.process.management.service.ProcessConfigDeleteAppService;
import moe.ahao.process.management.service.ProcessConfigEnableAppService;
import moe.ahao.process.management.service.ProcessConfigQueryService;
import moe.ahao.process.management.service.ProcessConfigSaveAppService;
import moe.ahao.process.management.service.ProcessConfigReleaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程配置 前端控制器
 */
@RestController
@RequestMapping("/process-config")
public class ProcessConfigController {
    @Autowired
    private ProcessConfigSaveAppService processConfigSaveAppService;
    @Autowired
    private ProcessConfigDeleteAppService processConfigDeleteAppService;
    @Autowired
    private ProcessConfigEnableAppService processConfigEnableAppService;
    @Autowired
    private ProcessConfigReleaseAppService processConfigReleaseAppService;

    @Autowired
    private ProcessConfigQueryService processConfigQueryService;

    /**
     * 保存流程配置
     *
     * @param command 请求
     * @return 响应
     */
    @PostMapping("/")
    public Result<Boolean> save(@RequestBody ProcessConfigSaveCommand command) {
        processConfigSaveAppService.save(command);
        return Result.success(true);
    }

    /**
     * 删除流程
     *
     * @param command 请求
     * @return 响应
     */
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody ProcessConfigDeleteCommand command) {
        processConfigDeleteAppService.delete(command);
        return Result.success(true);
    }

    /**
     * 流程配置详情
     *
     * @param name 流程节点名称
     * @return 响应
     */
    @GetMapping("/detail")
    public Result<ProcessConfigDetailDTO> detail(@RequestParam String name) {
        ProcessConfigDetailDTO dto = processConfigQueryService.detail(name);
        return Result.success(dto);
    }

    @PostMapping("/enable")
    public Result<Boolean> enable(@RequestBody ProcessConfigEnableCommand command) {
        processConfigEnableAppService.enable(command);
        return Result.success(true);
    }

    /**
     * 流程配置列表查询
     */
    @GetMapping("/list")
    public Result<List<ProcessConfigListDTO>> list() {
        List<ProcessConfigListDTO> list = processConfigQueryService.list(new ArrayList<>(ProcessEnableEnum.allowableValues()));
        return Result.success(list);
    }

    /**
     * 发布流程
     */
    @PostMapping("/release")
    public Result<Boolean> release() {
        processConfigReleaseAppService.release();
        return Result.success(true);
    }
}

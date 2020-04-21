package ${package.Controller};

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ${package.Service}.${table.serviceName};

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
<#if swagger2>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>

<#if swagger2>
@Api(tags = "${table.comment!}")
</#if>
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@Validated
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    @Autowired
    private ${table.serviceName} ${table.serviceName?uncap_first};

    @GetMapping("/detail")
    @ApiOperation("查看详情")
    public Object detail(@RequestParam Integer id) {
        return null;
    }

    @PostMapping("/list")
    @ApiOperation("查询售后单列表")
    public Object list(@Valid @RequestBody Object req) {
        return null;
    }

    @PostMapping("/save")
    @ApiOperation("保存售后单")
    public Integer save(@Valid @RequestBody Object req) {
        Integer id = null;
        return id;
    }
}
</#if>

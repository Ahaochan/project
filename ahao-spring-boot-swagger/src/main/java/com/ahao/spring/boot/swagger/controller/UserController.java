package com.ahao.spring.boot.swagger.controller;

import com.ahao.spring.boot.swagger.entity.User;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Api(value = "没用的属性", tags = {"功能分组1", "功能分组2"})
public class UserController {

    @ApiOperation(value = "@ApiOperation的简单使用", notes = "Api详细说明", hidden = false,
            response = User.class, responseContainer = "List",
            produces = "响应体(Response content type), application/json, application/xml",
            consumes = "请求体(Parameter content type), application/json, application/xml"
    )
    @GetMapping
    public List<User> apiOperation(@RequestBody User user) {
        return Collections.singletonList(new User());
    }

    @ApiOperation(value = "@ApiImplicitParam的简单使用",response = User.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", example = "1", required = true, dataTypeClass = Long.class, paramType = "path"),
            @ApiImplicitParam(name = "sex", value = "性别", example = "男", defaultValue = "男", allowableValues = "男, 女", required = true, dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", example = "abc", required = true, dataTypeClass = Long.class, paramType = "query")
    })
    @PostMapping(value = "/apiImplicitParam/{id}")
    public User apiImplicitParam(@PathVariable Long id, @RequestParam String sex, @RequestParam String password) {
        return new User();
    }

    @ApiOperation(value = "@ApiParam的简单使用")
    @PutMapping(value = "/apiParam/{id}")
    public User apiParam(@ApiParam(name = "id", value = "主键id", example = "1", required = true, type = "java.lang.Long") @PathVariable Long id,
                         @ApiParam(name = "sex", value = "性别", example = "男", defaultValue = "男", allowableValues = "男, 女", required = true, type = "java.lang.String") @RequestParam String sex,
                         @ApiParam(name = "password", value = "密码", example = "abc", required = true, type = "java.lang.Long") @RequestParam String password) {
        return new User();
    }


    @ApiOperation(value = "@ApiResponses的简单使用")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功", response = User.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "网站没找到", response = String.class),
            @ApiResponse(code = 404, message = "网站没找到", response = String.class),
            @ApiResponse(code = 500, message = "服务器错误", response = String.class)
    })
    @DeleteMapping(value = "/apiResponses")
    public Object apiResponses() {
        return new User();
    }
}

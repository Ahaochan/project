package moe.ahao.spring.boot.swagger.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户实体", description = "表示一个用户", parent = Object.class)
public class User {
    @ApiModelProperty(value = "主键id", dataType = "java.lang.Long", required = true, example ="1")
    private Long id;
    @ApiModelProperty(value = "名称", dataType = "java.lang.String", required = true, example ="张三")
    private String name;
    @ApiModelProperty(value = "性别", dataType = "java.lang.String", required = true, example ="女", allowableValues = "男, 女")
    private String age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

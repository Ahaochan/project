package moe.ahao.spring.boot.mybatis.plus.module.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * 1. 实现 IEnum 接口
 * 2. @EnumValue 注解
 */
public enum Sex implements IEnum<Integer> {
    man(1, "男"), woman(2, "女");

//    @EnumValue
    int code;
    String name;
    Sex(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return code;
    }
}

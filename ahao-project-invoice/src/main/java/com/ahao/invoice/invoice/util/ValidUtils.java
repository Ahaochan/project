package com.ahao.invoice.invoice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Ahaochan on 2017/8/6.
 */
public abstract class ValidUtils {
    private static final Logger logger = LoggerFactory.getLogger(ValidUtils.class);

    /**
     * 解析数据验证错误数据, 将其转换为以field为键, 错误信息Array为值的Map
     *
     * @param result 错误数据来源
     * @return 格式化的错误数据
     */
    public static Map<String, List<String>> paraseErrors(BindingResult result) {
        return result.getFieldErrors().stream()
                .peek(e -> logger.error("验证错误: " + e.getField() + ":" + e.getDefaultMessage()))
                // 以field进行分组
                .collect(Collectors.groupingBy(FieldError::getField,
                        // 将分组后的 List<FieldError> 转化为 List<String> , 存储 错误信息
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
    }
}

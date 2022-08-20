package moe.ahao.tend.consistency.core.infrastructure.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.ahao.exception.BizExceptionEnum;
@Getter
@AllArgsConstructor
public enum ConsistencyExceptionEnum implements BizExceptionEnum<ConsistencyException> {
    SYS_INTERNAL_SERVER_ERROR(10006, "系统内部错误"),
    ROCKSDB_PATH_IS_NOT_DIR(100000, "RocksDB初始化失败, 请指定文件夹而非文件: %s"),
    ROCKSDB_PATH_MKDIR_FAIL(100000, "RocksDB初始化失败, 创建RocksDB存储文件夹时失败: %s"),

    ;

    private final int code;
    private final String message;

    public ConsistencyException msg(Object... args) {
        return new ConsistencyException(code, String.format(message, args));
    }
}

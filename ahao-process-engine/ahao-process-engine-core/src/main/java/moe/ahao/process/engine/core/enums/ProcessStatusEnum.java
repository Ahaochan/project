package moe.ahao.process.engine.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcessStatusEnum {
    PROCESS("process"),
    ROLLBACK("rollback");
    private final String name;
}

package moe.ahao.process.engine.core.process;

import lombok.NoArgsConstructor;

/**
 * 标准流程动作
 */
@NoArgsConstructor
public abstract class StandardProcessor extends AbstractProcessor {
    public StandardProcessor(String name) {
        super(name);
    }
}

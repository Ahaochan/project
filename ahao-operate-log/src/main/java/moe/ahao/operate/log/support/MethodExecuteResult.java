package moe.ahao.operate.log.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MethodExecuteResult {
    boolean executeOk;
    private Throwable throwable;
    private String throwableMsg;
}

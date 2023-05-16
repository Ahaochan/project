package moe.ahao.process.engine.core.definition;

import lombok.Data;

import java.util.Objects;

/**
 * 流程关联的业务
 */
@Data
public class BizConfig {
    private String name;
    private Integer businessIdentifier;
    private Integer orderType;

    public BizConfig(Integer businessIdentifier, Integer orderType) {
        this.businessIdentifier = businessIdentifier;
        this.orderType = orderType;
    }

    public BizConfig(String name, Integer businessIdentifier, Integer orderType) {
        this.name = name;
        this.businessIdentifier = businessIdentifier;
        this.orderType = orderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BizConfig that = (BizConfig) o;
        return Objects.equals(businessIdentifier, that.businessIdentifier) && Objects.equals(orderType, that.orderType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessIdentifier, orderType);
    }
}

package moe.ahao.process.engine.wrapper.model;

import lombok.Data;

import java.util.Objects;

/**
 * 流程关联的业务
 */
@Data
public class BizConfigModel {
    private String name;
    private Integer businessIdentifier;
    private Integer orderType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BizConfigModel that = (BizConfigModel) o;
        return Objects.equals(businessIdentifier, that.businessIdentifier) && Objects.equals(orderType, that.orderType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessIdentifier, orderType);
    }
}

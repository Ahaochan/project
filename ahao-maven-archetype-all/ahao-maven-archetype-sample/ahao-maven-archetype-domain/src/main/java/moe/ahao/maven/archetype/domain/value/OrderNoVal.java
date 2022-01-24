package moe.ahao.maven.archetype.domain.value;

import moe.ahao.maven.archetype.common.domain.value.Val;

import java.util.Objects;

public class OrderNoVal implements Val {
    private final String no;
    public OrderNoVal(String no) {
        if(no == null || no.length() <= 0) {
            throw new IllegalArgumentException("订单编号不能为空");
        }
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderNoVal orderNoVal = (OrderNoVal) o;
        return Objects.equals(no, orderNoVal.no);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no);
    }
}

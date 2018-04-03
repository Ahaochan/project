package com.ahao.core.entity;

import com.ahao.core.util.CloneHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by Ahaochan on 2017/6/5.
 * <p>
 * 数据库中的数据必须有id和createTime和gmt_modify
 */
public class BaseDO {
    private Long id;
    private Date createTime;
    private Date modifyTime;

    public BaseDO() {
    }

    public BaseDO(Long id) {
        this.id = id;
    }

    public BaseDO(Long id, Date createTime, Date modifyTime) {
        this.id = id;
        this.createTime = CloneHelper.clone(createTime);
        this.modifyTime = CloneHelper.clone(modifyTime);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return CloneHelper.clone(createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = CloneHelper.clone(createTime);
    }

    public Date getModifyTime() {
        return CloneHelper.clone(modifyTime);
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = CloneHelper.clone(modifyTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BaseDO baseDO = (BaseDO) o;

        return new EqualsBuilder()
                .append(id, baseDO.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "BaseDO{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}

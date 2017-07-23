package com.ahao.entity;

import com.ahao.util.CloneHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Ahaochan on 2017/6/5.
 *
 * 数据库中的数据必须有id和createTime和gmt_modify
 */
public abstract class BaseDO {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    @Column(name = "gmt_create", updatable = false)
    private Date createTime;
    @Column(name = "gmt_modify")
    private Date modifyTime;

    public BaseDO(){
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

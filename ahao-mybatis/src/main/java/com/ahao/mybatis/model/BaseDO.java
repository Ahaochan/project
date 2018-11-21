package com.ahao.mybatis.model;

import com.ahao.commons.util.CloneHelper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by Ahaochan on 2017/6/5.
 * dao返回的entity父类, 也可用于Mybatis返回插入id
 * 返回插入id的Mybatis用法:
 * int saveUser(@Param("baseDO") BaseDO baseDO, @Param("name") String name);
 * <insert id="saveUser" useGeneratedKeys="true" keyProperty="baseDO.id" keyColumn="id">
 *     insert into user (name) values (#{name});
 * </insert>
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

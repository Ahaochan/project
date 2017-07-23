package com.ahao.invoice.admin.role.entity;

import com.ahao.entity.BaseDO;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by Avalon on 2017/6/3.
 *
 * invoice_role表的实体类
 */
@Alias("RoleDO")
@Table(name = "invoice_role")
public class RoleDO extends BaseDO {
    public static final String TAG =  "roleDO";


    @NotEmpty(message="{role.name.not.null}")
    @Size(max = 50, message = "{role.name.length.illegal}")
    private String name;
    @NotEmpty(message="{role.description.not.null}")
    @Size(max = 100, message = "{role.description.length.illegal}")
    private String description;
    private Boolean enabled;

    public RoleDO(){
    }

    public RoleDO(Long id, Date createTime, Date modifyTime, String name, String description, Boolean enabled) {
        super(id, createTime, modifyTime);
        this.name = name;
        this.description = description;
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "RoleDO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}

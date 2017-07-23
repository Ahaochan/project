package com.ahao.invoice.admin.auth.entity;

import com.ahao.entity.BaseDO;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by Avalon on 2017/5/9.
 *
 * invoice_auth表的实体类
 */
@Alias("AuthDO")
@Table(name = "invoice_auth")
public class AuthDO extends BaseDO {
    public static final String TAG =  "authDO";

    @NotEmpty(message="{auth.name.not.null}")
    @Size(max = 50, message = "{auth.name.length.illegal}")
    private String name;
    @NotEmpty(message="{auth.description.not.null}")
    @Size(max = 100, message = "{auth.description.length.illegal}")
    private String description;
    private Boolean enabled;

    public AuthDO(){
    }

    public AuthDO(Long id, Date createTime, Date modifyTime, String name, String description, Boolean enabled) {
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
        return "AuthDO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}

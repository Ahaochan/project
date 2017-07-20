package com.ahao.invoice.admin.user.entity;

import com.ahao.entity.BaseDO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.ibatis.type.Alias;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by Avalon on 2017/6/6.
 */
@Alias("UserDO")
@Table(name = "invoice_user")
public class UserDO extends BaseDO {
    public static final String TAG =  "userDO";


    @NotEmpty(message="{user.username.not.null}")
    @Size(max=20, message = "{user.username.length.illegal}")
    private String username;
    @NotEmpty(message="{user.password.not.null}")
    @Size(min = 5, max = 50, message = "{user.password.length.illegal}")
    private String password;
    private Date lastLoginTime;
    private String lastLoginIp;
    @Email(message = "{user.email.format.illegal}")
    private String email;

    private Boolean accountExpired = false;
    private Boolean accountLocked = false;
    private Boolean credentialsExpired = false;
    private Boolean enabled = false;

    public UserDO(){
    }

    public UserDO(Long id, Date createTime, Date modifyTime,
                  String username, String password,
                  Date lastLoginTime, String lastLoginIp, String email,
                  Boolean accountExpired, Boolean accountLocked, Boolean credentialsExpired, Boolean enabled) {
        super(id, createTime, modifyTime);
        this.username = username;
        this.password = password;
        this.lastLoginTime = (Date) lastLoginTime.clone();
        this.lastLoginIp = lastLoginIp;
        this.email = email;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.credentialsExpired = credentialsExpired;
        this.enabled = enabled;
    }

    public UserDO(UserDO user) {
        this(user.getId(), user.getCreateTime(), user.getModifyTime(),
                user.username, user.password,
                user.lastLoginTime, user.lastLoginIp, user.email,
                user.accountExpired, user.accountLocked, user.credentialsExpired, user.enabled);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLoginTime() {
        return (Date) lastLoginTime.clone();
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = (Date) lastLoginTime.clone();
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(Boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Boolean getCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserDO userDO = (UserDO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(username, userDO.username)
                .append(password, userDO.password)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(username)
                .append(password)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "UserDO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", email='" + email + '\'' +
                ", accountExpired=" + accountExpired +
                ", accountLocked=" + accountLocked +
                ", credentialsExpired=" + credentialsExpired +
                ", enabled=" + enabled +
                '}';
    }
}

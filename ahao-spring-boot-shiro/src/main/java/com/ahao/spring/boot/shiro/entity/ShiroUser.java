package com.ahao.spring.boot.shiro.entity;

import com.ahao.domain.entity.MybatisPlusBaseDO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

import java.io.Serializable;
import java.util.Date;

public class ShiroUser extends MybatisPlusBaseDO implements Serializable {
    private String username;
    private String email;
    private String password;
    private String salt;

    private Boolean locked;
    private Boolean disabled;
    private Boolean deleted;

    private Date expireTime;


    public static void assertShiro(ShiroUser user) {
        if(user == null) {
            throw new UnknownAccountException("该账号未被注册");
        }
        if(user.getDisabled() == null || user.getDisabled()) {
            throw new DisabledAccountException(String.format("该账号id=%s已被禁用", user.getId()));
        }
        if(user.getLocked() == null || user.getLocked()) {
            throw new LockedAccountException(String.format("该账号id=%s已被锁定", user.getId()));
        }
        Date expired = user.getExpireTime();
        if(expired != null) {
            boolean after = new Date().after(expired);
            if(after) {
                throw new ExpiredCredentialsException(String.format("该账号id=%s已过期", user.getId()));
            }
        }
    }

    // ====================== Getter And Setter ======================

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ShiroUser shiroUser = (ShiroUser) o;

        return new EqualsBuilder()
            .appendSuper(super.equals(o))
            .append(username, shiroUser.username)
            .append(email, shiroUser.email)
            .append(password, shiroUser.password)
            .append(salt, shiroUser.salt)
            .append(locked, shiroUser.locked)
            .append(disabled, shiroUser.disabled)
            .append(deleted, shiroUser.deleted)
            .append(expireTime, shiroUser.expireTime)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .appendSuper(super.hashCode())
            .append(username)
            .append(email)
            .append(password)
            .append(salt)
            .append(locked)
            .append(disabled)
            .append(deleted)
            .append(expireTime)
            .toHashCode();
    }

    @Override
    public String toString() {
        return "ShiroUser{" +
            "username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", salt='" + salt + '\'' +
            ", locked=" + locked +
            ", disabled=" + disabled +
            ", deleted=" + deleted +
            ", expireTime=" + expireTime +
            '}';
    }
}

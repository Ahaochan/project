package com.ahao.spring.boot.shiro.entity;

import com.ahao.domain.entity.BaseDO;
import com.ahao.util.commons.CloneHelper;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ShiroUser extends BaseDO implements Serializable {
    private Long id;
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return CloneHelper.clone(expireTime);
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = CloneHelper.clone(expireTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ShiroUser shiroUser = (ShiroUser) o;
        return Objects.equals(id, shiroUser.id) &&
            Objects.equals(username, shiroUser.username) &&
            Objects.equals(email, shiroUser.email) &&
            Objects.equals(password, shiroUser.password) &&
            Objects.equals(salt, shiroUser.salt) &&
            Objects.equals(locked, shiroUser.locked) &&
            Objects.equals(disabled, shiroUser.disabled) &&
            Objects.equals(deleted, shiroUser.deleted) &&
            Objects.equals(expireTime, shiroUser.expireTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, username, email, password, salt, locked, disabled, deleted, expireTime);
    }

    @Override
    public String toString() {
        return "ShiroUser{" +
            "id=" + id +
            ", username='" + username + '\'' +
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

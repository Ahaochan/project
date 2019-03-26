package com.ahao.rbac.shiro.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

import java.io.Serializable;
import java.util.Date;

public class ShiroUser implements Serializable {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String salt;

    private Boolean locked;
    private Boolean disabled;
    private Date expired;


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
        Date expired = user.getExpired();
        if(expired != null) {
            boolean after = new Date().after(expired);
            if(after) {
                throw new ExpiredCredentialsException(String.format("该账号id=%s已过期", user.getId()));
            }
        }
    }

    // ====================== Getter And Setter ======================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ShiroUser shiroUser = (ShiroUser) o;

        return new EqualsBuilder()
                .append(id, shiroUser.id)
                .append(username, shiroUser.username)
                .append(email, shiroUser.email)
                .append(password, shiroUser.password)
                .append(salt, shiroUser.salt)
                .append(locked, shiroUser.locked)
                .append(disabled, shiroUser.disabled)
                .append(expired, shiroUser.expired)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(username)
                .append(email)
                .append(password)
                .append(salt)
                .append(locked)
                .append(disabled)
                .append(expired)
                .toHashCode();
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
                ", expired=" + expired +
                '}';
    }
}

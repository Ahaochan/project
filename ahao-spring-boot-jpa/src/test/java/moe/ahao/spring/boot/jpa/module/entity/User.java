package moe.ahao.spring.boot.jpa.module.entity;

import com.ahao.domain.entity.JPABaseDO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity(name = "User")
@Table(name = "user")
public class User extends JPABaseDO implements Serializable {
    private String username;
    private String email;
    private String password;
    private String salt;
    private int sex;

    @Column(name = "is_locked")
    private Boolean locked;
    @Column(name = "is_disabled")
    private Boolean disabled;
    @Column(name = "is_deleted")
    private Boolean deleted;

    private Date expireTime;

    @Transient
    private String transientValue;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getTransientValue() {
        return transientValue;
    }

    public void setTransientValue(String transientValue) {
        this.transientValue = transientValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return sex == user.sex &&
            Objects.equals(username, user.username) &&
            Objects.equals(email, user.email) &&
            Objects.equals(password, user.password) &&
            Objects.equals(salt, user.salt) &&
            Objects.equals(locked, user.locked) &&
            Objects.equals(disabled, user.disabled) &&
            Objects.equals(deleted, user.deleted) &&
            Objects.equals(expireTime, user.expireTime) &&
            Objects.equals(transientValue, user.transientValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, email, password, salt, sex, locked, disabled, deleted, expireTime, transientValue);
    }

    @Override
    public String toString() {
        return "User{" +
            "username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", salt='" + salt + '\'' +
            ", sex=" + sex +
            ", locked=" + locked +
            ", disabled=" + disabled +
            ", deleted=" + deleted +
            ", expireTime=" + expireTime +
            ", transientValue='" + transientValue + '\'' +
            "} " + super.toString();
    }
}

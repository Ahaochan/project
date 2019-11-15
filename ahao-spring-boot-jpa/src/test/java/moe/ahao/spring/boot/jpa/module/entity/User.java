package moe.ahao.spring.boot.jpa.module.entity;

import com.ahao.domain.entity.JPABaseDO;
import moe.ahao.spring.boot.jpa.module.enums.Sex;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


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

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public User setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public Sex getSex() {
        return Sex.parse(sex);
    }

    public User setSex(Sex sex) {
        this.sex = sex.getCode();
        return this;
    }

    public Boolean getLocked() {
        return locked;
    }

    public User setLocked(Boolean locked) {
        this.locked = locked;
        return this;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public User setDisabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public User setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public User setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder()
            .appendSuper(super.equals(o))
            .append(username, user.username)
            .append(email, user.email)
            .append(password, user.password)
            .append(salt, user.salt)
            .append(sex, user.sex)
            .append(locked, user.locked)
            .append(disabled, user.disabled)
            .append(deleted, user.deleted)
            .append(expireTime, user.expireTime)
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
            .append(sex)
            .append(locked)
            .append(disabled)
            .append(deleted)
            .append(expireTime)
            .toHashCode();
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
            '}';
    }
}

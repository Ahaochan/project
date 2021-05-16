package moe.ahao.spring.boot.mybatis.tk.module.entity;

import moe.ahao.domain.entity.BaseDO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import tk.mybatis.mapper.annotation.KeySql;
import tk.mybatis.mapper.code.IdentityDialect;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "user")
public class User extends BaseDO implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true, dialect = IdentityDialect.MYSQL) // 自增主键
    private Long id;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String salt;
    @Column
    private Integer sex;

    @Column(name = "is_locked")
    private Boolean locked;
    @Column(name = "is_disabled")
    private Boolean disabled;
    @Column(name = "is_deleted")
    private Boolean deleted;

    @Column
    private Date expireTime;

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

    public Integer getSex() {
        return sex;
    }

    public User setSex(Integer sex) {
        this.sex = sex;
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
            "id=" + id +
            ", username='" + username + '\'' +
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

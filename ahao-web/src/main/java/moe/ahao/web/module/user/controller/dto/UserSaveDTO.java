package moe.ahao.web.module.user.controller.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class UserSaveDTO {
    public interface Update { }
    public interface Add { }

    @NotNull(groups = Update.class, message = "新增操作时, id不能为null")
    @Null(groups = Add.class, message = "新增操作时, id必须为null")
    private Long id;
    @Size(min = 3, max = 20)
    private String username;
    private String email;
    private String password;

    public Long getId() {
        return id;
    }

    public UserSaveDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserSaveDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserSaveDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserSaveDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        return "UserSaveDTO{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}

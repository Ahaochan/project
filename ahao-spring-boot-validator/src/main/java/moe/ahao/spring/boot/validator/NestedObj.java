package moe.ahao.spring.boot.validator;

import javax.validation.Valid;
import javax.validation.constraints.Max;

public class NestedObj {
    @Max(value = 10, message = "id不大于10")
    private Integer id;

    @Valid
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package moe.ahao.spring.boot.jpa.module.entity;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> sexEq(Integer sex) {
        return (root, query, cb) -> cb.equal(root.get("sex"), sex);
    }
    public static Specification<User> sexIn(Integer... sex) {
        return (root, query, cb) -> root.get("sex").in((Object[]) sex);
    }
    public static Specification<User> usernameAllLike(String username) {
        return (root, query, cb) -> cb.like(root.get("username"), "%" + username + "%");
    }
}

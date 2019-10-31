package moe.ahao.spring.boot.jpa.module.mapper;


import moe.ahao.spring.boot.jpa.module.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserMapper extends JpaRepository<User, Long> {
    List<User> getUsersByUsernameLike(String like);

    @Query(value = "select ifnull(count(*), 0) from user", nativeQuery = true)
    int getCount();
}

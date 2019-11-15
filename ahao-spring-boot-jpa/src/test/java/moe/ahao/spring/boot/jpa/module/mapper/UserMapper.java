package moe.ahao.spring.boot.jpa.module.mapper;


import moe.ahao.spring.boot.jpa.module.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserMapper extends JpaRepository<User, Long> {
    List<User> getUsersByUsernameLike(String like);

    @Query(value = "select ifnull(count(*), 0) from `user`", nativeQuery = true)
    int getCount();

    @Query(value = "select * from user where create_time > ?1", nativeQuery = true)
    List<User> findByCreateTimeAfter(Date after);// 此方法sql将会报错(java.lang.IllegalArgumentException)，看出原因了吗,若没看出来，请看下一个例子

    @Query("select u from User u where u.createTime between ?1 and ?2")
    List<User> findByCreateTimeBetween(Date start, Date end);

    @Query("select new User(u.username, u.email) from User u where u.username like %:name%")
    List<User> findByNameAllLike(@Param("name") String username);
}

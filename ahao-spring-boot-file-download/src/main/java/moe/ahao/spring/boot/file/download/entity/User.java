package moe.ahao.spring.boot.file.download.entity;

import moe.ahao.util.commons.lang.RandomHelper;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Integer id;
    private String name;
    private String address;
    private Integer age;

    public static List<User> list(int size) {
        List<User> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            User user = new User();
            user.id = i;
            user.name = "Admin" + i;
            user.address = "中国";
            user.age = RandomHelper.getInt(size);
            result.add(user);
        }
        return result;
    }

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public User setAge(Integer age) {
        this.age = age;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}

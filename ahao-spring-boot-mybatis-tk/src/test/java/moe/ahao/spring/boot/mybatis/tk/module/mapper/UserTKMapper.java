package moe.ahao.spring.boot.mybatis.tk.module.mapper;


import moe.ahao.spring.boot.mybatis.tk.CommonMapper;
import moe.ahao.spring.boot.mybatis.tk.module.entity.UserTK;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

public interface UserTKMapper extends CommonMapper<UserTK> {
    @Select("select * from user;")
    Cursor<UserTK> selectCursorAll();
}

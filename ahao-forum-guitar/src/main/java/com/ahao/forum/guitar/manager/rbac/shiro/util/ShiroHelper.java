package com.ahao.forum.guitar.manager.rbac.shiro.util;

import com.ahao.core.config.SpringConfig;
import com.ahao.core.entity.DataSet;
import com.ahao.core.entity.IDataSet;
import org.apache.shiro.SecurityUtils;
import org.springframework.jdbc.core.JdbcTemplate;

public class ShiroHelper {

    private static volatile JdbcTemplate jdbcTemplate;

    private static synchronized JdbcTemplate dao(){
        if(jdbcTemplate == null){
            jdbcTemplate = SpringConfig.instance().getBean("jdbcTemplate");
        }
        return jdbcTemplate;
    }

    public static long getMyUserId(){
        return getCurrentUser().getLong("id");
    }

    public static int getMyUserWeight(){
        return getCurrentUser().getInt("weight");
    }

    public static boolean isRoot(){
        int maxWeight = getMaxWeight();
        int weight = ShiroHelper.getMyUserWeight();
        return weight >= maxWeight;
    }

    public static int getMaxWeight(){
        String sql = "select max(IFNULL(r.weight, 0)) weight from admin_role r where r.enabled = 1";
        return new DataSet(dao().queryForMap(sql)).getInt("weight");
    }

    public static IDataSet getCurrentUser(){
        return (IDataSet) SecurityUtils.getSubject().getPrincipal();
    }
}

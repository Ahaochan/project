package com.ahao.invoice.product.category.entity;

import com.ahao.entity.BaseDO;
import org.apache.ibatis.type.Alias;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Ahaochan on 2017/8/21.
 */
@Alias("CategoryDO")
@Table(name = "product_category")
public class CategoryDO extends BaseDO {
    public static final String TAG =  "categoryDO";

    private String name;
    private String description;

    public CategoryDO(){

    }

    public CategoryDO(Long id, Date createTime, Date modifyTime, String name, String description) {
        super(id, createTime, modifyTime);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CategoryDO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

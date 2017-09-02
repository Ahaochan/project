package com.ahao.invoice.product.goods.entity;

import com.ahao.entity.BaseDO;
import org.apache.ibatis.type.Alias;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Ahaochan on 2017/8/22.
 */
@Alias("GoodsDO")
@Table(name = "product_goods")
public class GoodsDO extends BaseDO{
    public static final String TAG =  "goodsDO";

    private String name;
    private Long categoryId;
    private String specification;
    private String unit;
    private Double unitePrice;
    private Double taxRate;

    public GoodsDO(){

    }

    public GoodsDO(Long id, Date createTime, Date modifyTime, String name, Long categoryId, String specification, String unit, Double unitePrice, Double taxRate) {
        super(id, createTime, modifyTime);
        this.name = name;
        this.categoryId = categoryId;
        this.specification = specification;
        this.unit = unit;
        this.unitePrice = unitePrice;
        this.taxRate = taxRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getUnitePrice() {
        return unitePrice;
    }

    public void setUnitePrice(Double unitePrice) {
        this.unitePrice = unitePrice;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public String toString() {
        return "GoodsDO{" +
                "name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", specification='" + specification + '\'' +
                ", unit='" + unit + '\'' +
                ", unitePrice=" + unitePrice +
                ", taxRate='" + taxRate + '\'' +
                '}';
    }
}

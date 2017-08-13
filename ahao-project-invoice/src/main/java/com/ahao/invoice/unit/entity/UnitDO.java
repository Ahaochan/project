package com.ahao.invoice.unit.entity;

import com.ahao.entity.BaseDO;
import org.apache.ibatis.type.Alias;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Ahaochan on 2017/7/23.
 * <p>
 * invoice_unit表的实体类, 表示购销单位
 */
@Alias("UnitDO")
@Table(name = "invoice_unit")
public class UnitDO extends BaseDO {
    public static final String TAG = "unitDO";

    private String name;
    private String taxId;
    private String address;
    private String tel;
    private String account;

    public UnitDO() {
    }

    public UnitDO(Long id, Date createTime, Date modifyTime, String name, String taxId, String address, String tel, String account) {
        super(id, createTime, modifyTime);
        this.name = name;
        this.taxId = taxId;
        this.address = address;
        this.tel = tel;
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "UnitDO{" +
                "name='" + name + '\'' +
                ", taxId='" + taxId + '\'' +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}

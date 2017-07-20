package com.ahao.invoice.base;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by Avalon on 2017/6/6.
 */

public class IndexView {
    public static final String TAG =  "indexView";


    private Date nowDate;
    private String username;
    private String loginIp;

    public IndexView(){
    }

    public IndexView(Date nowDate, String username, String loginIp) {
        this.nowDate = (Date) nowDate.clone();
        this.username = username;
        this.loginIp = loginIp;
    }

    public Date getNowDate() {
        return (Date) nowDate.clone();
    }

    public void setNowDate(Date nowDate) {
        this.nowDate = (Date) nowDate.clone();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        IndexView indexView = (IndexView) o;

        return new EqualsBuilder()
                .append(nowDate, indexView.nowDate)
                .append(username, indexView.username)
                .append(loginIp, indexView.loginIp)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(nowDate)
                .append(username)
                .append(loginIp)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "IndexView{" +
                "nowDate=" + nowDate +
                ", username='" + username + '\'' +
                ", loginIp='" + loginIp + '\'' +
                '}';
    }
}

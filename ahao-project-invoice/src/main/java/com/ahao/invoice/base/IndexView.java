package com.ahao.invoice.base;

import com.ahao.util.CloneHelper;

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
        this.nowDate = CloneHelper.clone(nowDate);
        this.username = username;
        this.loginIp = loginIp;
    }

    public Date getNowDate() {
        return CloneHelper.clone(nowDate);
    }

    public void setNowDate(Date nowDate) {
        this.nowDate = CloneHelper.clone(nowDate);
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
    public String toString() {
        return "IndexView{" +
                "nowDate=" + nowDate +
                ", username='" + username + '\'' +
                ", loginIp='" + loginIp + '\'' +
                '}';
    }
}

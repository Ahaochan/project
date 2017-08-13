package com.ahao.invoice.invoice.entity;

import com.ahao.entity.BaseDO;
import com.ahao.util.CloneHelper;

import java.util.Date;

/**
 * Created by Ahaochan on 2017/7/29.
 */
public class InvoiceDO extends BaseDO {
    private String code;
    private String number;
    private Date date;
    private String verifyCode;
    private Long purchasesUnitId;
    private Long salesUnitId;
    private String passwordArea;
    private String mema;
    private Long payeeId;
    private Long reviewId;
    private Long drawerId;

    public InvoiceDO() {

    }

    public InvoiceDO(Long id, Date createTime, Date modifyTime, String code, String number, Date date, String verifyCode, Long purchasesUnitId, Long salesUnitId, String passwordArea, String mema, Long payeeId, Long reviewId, Long drawerId) {
        super(id, createTime, modifyTime);
        this.code = code;
        this.number = number;
        this.date = CloneHelper.clone(date);
        this.verifyCode = verifyCode;
        this.purchasesUnitId = purchasesUnitId;
        this.salesUnitId = salesUnitId;
        this.passwordArea = passwordArea;
        this.mema = mema;
        this.payeeId = payeeId;
        this.reviewId = reviewId;
        this.drawerId = drawerId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return CloneHelper.clone(date);
    }

    public void setDate(Date date) {
        this.date = CloneHelper.clone(date);
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Long getPurchasesUnitId() {
        return purchasesUnitId;
    }

    public void setPurchasesUnitId(Long purchasesUnitId) {
        this.purchasesUnitId = purchasesUnitId;
    }

    public Long getSalesUnitId() {
        return salesUnitId;
    }

    public void setSalesUnitId(Long salesUnitId) {
        this.salesUnitId = salesUnitId;
    }

    public String getPasswordArea() {
        return passwordArea;
    }

    public void setPasswordArea(String passwordArea) {
        this.passwordArea = passwordArea;
    }

    public String getMema() {
        return mema;
    }

    public void setMema(String mema) {
        this.mema = mema;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getDrawerId() {
        return drawerId;
    }

    public void setDrawerId(Long drawerId) {
        this.drawerId = drawerId;
    }

    @Override
    public String toString() {
        return "InvoiceDO{" +
                "code='" + code + '\'' +
                ", number='" + number + '\'' +
                ", date=" + date +
                ", verifyCode='" + verifyCode + '\'' +
                ", purchasesUnitId=" + purchasesUnitId +
                ", salesUnitId=" + salesUnitId +
                ", passwordArea='" + passwordArea + '\'' +
                ", mema='" + mema + '\'' +
                ", payeeId=" + payeeId +
                ", reviewId=" + reviewId +
                ", drawerId=" + drawerId +
                '}';
    }
}

package com.gproject.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class OrderEntity implements Serializable {

    long id;
    String orderid;
    double totalprice;
    String cphone;
    String cname;
    Date time;
    private String payway;
    String eatway;
    boolean iseatway;
    boolean isdeal = false;
    String remark;
    List<ProductListEntity.ProductEntity>productEntities;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public String getCphone() {
        return cphone;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getEatway() {
        return eatway;
    }

    public void setEatway(String eatway) {
        this.eatway = eatway;
    }

    public boolean isIseatway() {
        return iseatway;
    }

    public void setIseatway(boolean iseatway) {
        this.iseatway = iseatway;
    }

    public boolean isIsdeal() {
        return isdeal;
    }

    public void setIsdeal(boolean isdeal) {
        this.isdeal = isdeal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ProductListEntity.ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductListEntity.ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }
}

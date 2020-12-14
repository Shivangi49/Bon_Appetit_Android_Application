package com.example.bonappetit.model;

public class Payment {
    String id,orderid;

    public Payment() {
    }

    public Payment(String id, String orderid) {
        this.id = id;
        this.orderid = orderid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}

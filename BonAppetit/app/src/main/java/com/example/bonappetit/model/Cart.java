package com.example.bonappetit.model;

public class Cart {
    private String name,price,quantity,total,foodid;

    public Cart() {
    }

    public Cart(String name, String price, String foodid, String quantity,String total) {
        this.name = name;
        this.price = price;
        this.foodid = foodid;
        this.quantity = quantity;
        this.total=total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

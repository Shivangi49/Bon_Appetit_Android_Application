package com.example.bonappetit.model;

public class Food {

    private  String name;
    private  String image;
    private  String price;
    private  String category_id;


    public Food() {
    }

    public Food(String category_id,String name,String image,String price) {
        this.category_id = category_id;
        this.price = price;
        this.image=image;
        this.name=name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}

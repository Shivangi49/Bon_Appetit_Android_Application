package com.example.bonappetit.model;

public class User {
    private String feedback,name;

    public User() {
    }

    public User(String feedback, String name) {
        this.feedback = feedback;
        this.name = name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.iiitnr.libraryapp;

public class User {

    private String name, id,email;
    private int enroll, card,  type;


    public User(String name, String email, int enroll, int card, int type) {
        this.name = name;
        this.email = email;
        this.enroll = enroll;
        this.card = card;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEnroll() {
        return enroll;
    }

    public void setEnroll(int enroll) {
        this.enroll = enroll;
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

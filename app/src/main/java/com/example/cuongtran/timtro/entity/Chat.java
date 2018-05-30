package com.example.cuongtran.timtro.entity;

import com.google.firebase.Timestamp;

public class Chat {
    private String user,text;
    private Timestamp timestamp;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Chat(String user, String text, Timestamp timestamp) {

        this.user = user;
        this.text = text;
        this.timestamp = timestamp;
    }
    public Chat(){

    }
}

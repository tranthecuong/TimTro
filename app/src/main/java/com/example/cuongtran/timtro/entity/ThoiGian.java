package com.example.cuongtran.timtro.entity;

import com.google.firebase.Timestamp;

public class ThoiGian {
    Timestamp timestamp;

    public ThoiGian(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public ThoiGian(){

    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

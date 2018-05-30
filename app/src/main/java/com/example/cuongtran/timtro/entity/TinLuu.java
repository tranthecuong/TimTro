package com.example.cuongtran.timtro.entity;

public class TinLuu extends Model {
    private String idtaikhoan;
    private String idtin;

    public TinLuu(String idtaikhoan, String idtin) {
        this.idtaikhoan = idtaikhoan;
        this.idtin = idtin;
    }
    public TinLuu(){

    }

    public String getIdtaikhoan() {
        return idtaikhoan;
    }

    public void setIdtaikhoan(String idtaikhoan) {
        this.idtaikhoan = idtaikhoan;
    }

    public String getIdtin() {
        return idtin;
    }

    public void setIdtin(String idtin) {
        this.idtin = idtin;
    }
}

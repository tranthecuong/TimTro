package com.example.cuongtran.timtro.entity;

public class ThongBao {
    private String idNhaTro;
    private String idUser;
    private long created;

    public ThongBao(String idNhaTro, String idUser, long time) {
        this.idNhaTro = idNhaTro;
        this.idUser = idUser;
        this.created=time;
    }
    public ThongBao(){
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {

        this.created = created;
    }

    public String getIdNhaTro() {

        return idNhaTro;
    }

    public void setIdNhaTro(String idNhaTro) {
        this.idNhaTro = idNhaTro;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}

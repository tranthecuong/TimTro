package com.example.cuongtran.timtro.entity;

import com.google.firebase.Timestamp;

public class TinDang extends Model {
    private String anh,chitietdiachi,dienthoai,tenquanhuyen,idtaikhoan,tenthanhpho,kinhdo,vido,mota ;
    private long gia,dientich;
    private Timestamp ngaydang;
    public TinDang(){};

    public TinDang(String anh, String chitietdiachi, String dienthoai, long dientich, long gia, String idquanhuyen, String idtaikhoan, String idthanhpho, String kinhdo, String vido, String mota, Timestamp ngaydang) {
        this.anh = anh;
        this.chitietdiachi = chitietdiachi;
        this.dienthoai = dienthoai;
        this.dientich = dientich;
        this.gia = gia;
        this.tenquanhuyen = idquanhuyen;
        this.idtaikhoan = idtaikhoan;
        this.tenthanhpho = idthanhpho;
        this.kinhdo = kinhdo;
        this.vido = vido;
        this.mota = mota;
        this.ngaydang = ngaydang;
    }

    public String getAnh() {
        return anh;
    }

    public String getChitietdiachi() {
        return chitietdiachi;
    }

    public String getDienthoai() {
        return dienthoai;
    }

    public long getDientich() {
        return dientich;
    }

    public long getGia() {
        return gia;
    }

    public String gettenquanhuyen() {
        return tenquanhuyen;
    }

    public String getIdtaikhoan() {
        return idtaikhoan;
    }

    public String gettenthanhpho() {
        return tenthanhpho;
    }

    public String getKinhdo() {
        return kinhdo;
    }

    public String getVido() {
        return vido;
    }

    public String getMota() {
        return mota;
    }

    public Timestamp getNgaydang() {
        return ngaydang;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public void setChitietdiachi(String chitietdiachi) {
        this.chitietdiachi = chitietdiachi;
    }

    public void setDienthoai(String dienthoai) {
        this.dienthoai = dienthoai;
    }

    public void setDientich(long dientich) {
        this.dientich = dientich;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }

    public void settenquanhuyen(String idquanhuyen) {
        this.tenquanhuyen = idquanhuyen;
    }

    public void setIdtaikhoan(String idtaikhoan) {
        this.idtaikhoan = idtaikhoan;
    }

    public void settenthanhpho(String idthanhpho) {
        this.tenthanhpho = idthanhpho;
    }

    public void setKinhdo(String kinhdo) {
        this.kinhdo = kinhdo;
    }

    public void setVido(String vido) {
        this.vido = vido;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public void setNgaydang(Timestamp ngaydang) {
        this.ngaydang = ngaydang;
    }
}

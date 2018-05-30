package com.example.cuongtran.timtro.presenter;

import com.example.cuongtran.timtro.model.data.ModelChiTietBanDo;

public class PresenterChiTietBanDo implements ModelChiTietBanDo.Icallback {
    ModelChiTietBanDo modelChiTietBanDo;
    IChiTiet iChiTiet;
    public PresenterChiTietBanDo(IChiTiet iChiTiet){
        this.iChiTiet=iChiTiet;
        modelChiTietBanDo= new ModelChiTietBanDo(this);
    }

    public void getData(String idnhanduoc){
        modelChiTietBanDo.getData(idnhanduoc);
    };

    @Override
    public void result(String diaChi, String kinhdo, String vido) {
        iChiTiet.success(diaChi, kinhdo, vido);
        iChiTiet.hideDialog();
    }


    public interface IChiTiet{
        void hideDialog();
        void success(String diaChi,String kinhdo,String vido);
    }
}

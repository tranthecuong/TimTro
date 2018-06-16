package com.example.cuongtran.timtro.presenter;

import com.example.cuongtran.timtro.entity.TinDang;
import com.example.cuongtran.timtro.model.data.ModelLocTin;

import java.util.ArrayList;

public class PresenterLocTin implements ModelLocTin.ICallback {
    IView iView;
    ModelLocTin modelLocTin;
    public PresenterLocTin(IView iView){
        this.iView=iView;
        modelLocTin= new ModelLocTin(this);
    }

    public void updateFilter(final String tinh, final String huyen, long mingia, long maxgia, final long minS, final long maxS) {

        if(tinh.equals("Tất cả")&&huyen.equals("Tất cả")){
            // truy van gia dien tich
            modelLocTin.truyVan1(mingia,maxgia,minS,maxS);
        }
        else {
            // truy van gia, dien tich, huyen
            if(tinh.equals("Tất cả")){
                modelLocTin.truyVan2(huyen,mingia,maxgia,minS,maxS);
            }
            if(huyen.equals("Tất cả")){
                // truy van gia, dien tich, tinh
                modelLocTin.truyVan3(tinh,mingia,maxgia,minS,maxS);
            }

            if(!huyen.equals("Tất cả")&&!tinh.equals("Tất cả")){
                // truy van gia, dien tich
                modelLocTin.truyVan4(tinh,huyen,mingia,maxgia,minS,maxS);
            }

        }

    }

    @Override
    public void truyVanSuccess(ArrayList<TinDang> arrayList) {
        iView.truyVanSuccess(arrayList);
    }

    public  interface IView{

        void truyVanSuccess(ArrayList<TinDang> arrayList);
    }
}

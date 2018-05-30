package com.example.cuongtran.timtro.presenter;

import android.content.Context;

import com.example.cuongtran.timtro.entity.CheckConnection;
import com.example.cuongtran.timtro.entity.TinDang;
import com.example.cuongtran.timtro.model.data.ModelXemTinMap;

import java.util.ArrayList;

public class PresenterXemTinMap implements ModelXemTinMap.IListener {
    IView iView;
    ModelXemTinMap modelXemTinMap;
    public PresenterXemTinMap(IView iView){
        this.iView=iView;
        modelXemTinMap= new ModelXemTinMap(this);
    }
    public  void checkInternet(Context context){
        if (CheckConnection.haveNetworkConnection(context)) {
            //neu co mang;
           iView.haveInternet(true);

        } else {
            iView.haveInternet(false);
        }


    }
    public void getData(){
        modelXemTinMap.getData();
    }

    @Override
    public void getDataSuccess(ArrayList<TinDang> arr) {
        iView.setData(arr);

    }

    public interface IView{
        void setData(ArrayList<TinDang> arr);
        void haveInternet(boolean status);
    }
}

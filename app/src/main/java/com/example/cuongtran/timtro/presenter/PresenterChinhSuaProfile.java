package com.example.cuongtran.timtro.presenter;

import android.widget.Toast;

import com.example.cuongtran.timtro.model.data.ModelChinhSuaProfile;

public class PresenterChinhSuaProfile implements ModelChinhSuaProfile.ICallBack {
    IView iView;
    ModelChinhSuaProfile modelChinhSuaProfile;
    public PresenterChinhSuaProfile(IView iView){
        this.iView=iView;
        modelChinhSuaProfile= new ModelChinhSuaProfile(this);
    }

    @Override
    public void onSuccess(String mess) {
        iView.showMess(mess);
        iView.showProgress(false);
    }

    public interface IView{
        void showMess(String mess);
        void showProgress(boolean vis);

    }
    public void suaProfile(String id,String ten,String sdt,String dc){
        if(ten.isEmpty()||sdt.isEmpty()||dc.isEmpty()){
            iView.showMess("Các trường không được để trống !");
        }
        else {
            modelChinhSuaProfile.suaProfile(id,ten,sdt,dc);
            iView.showProgress(true);
        }
    }

}

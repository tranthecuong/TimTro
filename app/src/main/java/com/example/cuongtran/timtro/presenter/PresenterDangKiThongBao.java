package com.example.cuongtran.timtro.presenter;

import android.content.Context;
import com.example.cuongtran.timtro.entity.CheckConnection;
import com.example.cuongtran.timtro.model.data.ModelDangKyThongBao;

import java.util.ArrayList;

public class PresenterDangKiThongBao implements ModelDangKyThongBao.IListener {
    Iview iview;
    ModelDangKyThongBao modelDangKyThongBao;


    public PresenterDangKiThongBao(Iview iview){
        this.iview=iview;
        modelDangKyThongBao= new ModelDangKyThongBao(this);

    }
    public void checkInternet(Context context){
        if (CheckConnection.haveNetworkConnection(context)) {

        } else {
            iview.showMess("Kiểm tra lại internet");
        }

    };

    public void turnOffNotification(){

        modelDangKyThongBao.turnOffNotification();

    };
    public  void turnOnNotification(ArrayList<String> arr){
        modelDangKyThongBao.turnOnNotification(arr);
    }
    //kiem tra trang thai dang ki cua nguoi dung
    public  void checkTrangThai(){
        modelDangKyThongBao.checkTrangThai();
    }


    @Override
    public void showMess(String mess) {
        iview.showMess(mess);
    }

    @Override
    public void setVisibility(int i) {
        iview.setVisibility(i);
    }

    public interface Iview{
        void showMess(String mess);
        void setVisibility(int i);
    }
}

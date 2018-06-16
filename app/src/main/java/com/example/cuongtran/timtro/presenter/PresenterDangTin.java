package com.example.cuongtran.timtro.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import com.example.cuongtran.timtro.entity.CheckConnection;
import com.example.cuongtran.timtro.model.data.ModelDangTin;
import com.google.android.gms.maps.model.LatLng;

public class PresenterDangTin implements ModelDangTin.ICallBack {
    IView iView;
    ModelDangTin modelDangTin;
public PresenterDangTin (IView iView){
    this.iView=iView;
    modelDangTin= new ModelDangTin(this);
}

    @Override
    public void onFail(String mess) {
        iView.showMess(mess);
        iView.showLoading(false);
    }

    @Override
    public void onSuccess(String mess) {
    iView.showMess(mess);
    iView.showLoading(false);
    iView.done();
    }

    public interface IView{
    void showMess(String mess);
    void showLoading(Boolean vis);
    void done();
}
public void dangTin(Context mContext,String idQuanHuyen, String idThanhPho, String chiTietDiaChi, String gia, String dienTich, String dienThoai, String moTa, Bitmap resizedBitmap, LatLng mLatLng){

    if(CheckConnection.haveNetworkConnection(mContext)){

        if (resizedBitmap == null) {
            iView.showMess("Chưa chọn ảnh !");
        } else
        if(mLatLng==null){
            iView.showMess("Hãy chọn địa chỉ của bạn trên bản đồ");
        }
        else

        {
            if (chiTietDiaChi.equals("") || gia.equals("") || dienTich.equals("") || dienThoai.equals("")
                    || moTa.equals("")) {
                iView.showMess("Nhập đầy đủ các trường");
            } else {
                iView.showLoading(true);
                modelDangTin.dangTin(resizedBitmap,idThanhPho,idQuanHuyen,chiTietDiaChi,gia,dienTich,dienThoai,moTa,mLatLng);
            }
        }


    }else {
        iView.showMess("Kiểm tra lại kết nối internet");
    }
}

}

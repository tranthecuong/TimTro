package com.example.cuongtran.timtro.presenter;

import com.example.cuongtran.timtro.entity.ThongBao;
import com.example.cuongtran.timtro.model.data.ModelXemThongBaoFragment;

import java.util.List;

public class PresenterXemThongBaoFragment implements ModelXemThongBaoFragment.ICallBack {
    IView iView;
    ModelXemThongBaoFragment modelXemThongBaoFragment;
    public PresenterXemThongBaoFragment(IView iView){
        this.iView=iView;
        modelXemThongBaoFragment=new ModelXemThongBaoFragment(this);
    }

    public void getData(String idCurrentUser) {
        modelXemThongBaoFragment.getData(idCurrentUser);
    }

    @Override
    public void getDataSuccess(List<ThongBao> arrayList) {
        iView.setData(arrayList);
    }

    public interface IView{
        void setData(List<ThongBao> arrayList);
    }
}

package com.example.cuongtran.timtro.presenter;

import com.example.cuongtran.timtro.model.data.ModelManageProfileFragment;

public class PresenterManageProfileFragment implements ModelManageProfileFragment.ICallBack {
    IView iView;
    ModelManageProfileFragment modelManageProfileFragment;
    public PresenterManageProfileFragment(IView iView){
       this.iView=iView;
       modelManageProfileFragment=new ModelManageProfileFragment(this);
    }

    public void getData(String id) {
        modelManageProfileFragment.getData(id);
    }

    @Override
    public void returnData(String name, String url) {
        iView.setData(name,url);
    }

    public interface IView{
        void setData(String name, String url);
    }
}

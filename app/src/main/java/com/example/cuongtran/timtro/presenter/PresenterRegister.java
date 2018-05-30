package com.example.cuongtran.timtro.presenter;

import com.example.cuongtran.timtro.model.data.ModelRegister;

public class PresenterRegister implements ModelRegister.Ipresenter {
    IRegisterView iRegisterView;
    ModelRegister modelRegister;

    public PresenterRegister(IRegisterView iRegisterView){
        this.iRegisterView=iRegisterView;
        modelRegister= new ModelRegister(this);

    }

    public void register(String mail,String ten,String pass,String repass ){
        if(ten.equals("")||mail.equals("")||pass.equals("")||repass.equals("")){
            //Neu nhap thieu
            iRegisterView.showMessage("Hãy nhập vào đầy đủ !");

        }
        else {

            if(pass.equals(repass)){
                //neu repass dung
                //xu ly
                modelRegister.signup(ten,mail,pass);
                iRegisterView.showDialog(true);
                //signup(hoten,user, pass);
            }
            else {
                iRegisterView.showMessage("Nhập lại mật khẩu không đúng");
                //Toast.makeText(RegisterActivity.this, "Nhập lại mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public void onSuccess() {
        iRegisterView.success();
    }

    @Override
    public void onError(String mess) {
        iRegisterView.showMessage(mess);
        iRegisterView.showDialog(false);
    }

    public interface IRegisterView{
        void showMessage(String mess);
        void showDialog(boolean vis);
        void success();
    }
}

package com.example.cuongtran.timtro.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.example.cuongtran.timtro.model.authenticate.FirebaseAuthenticator;
import com.example.cuongtran.timtro.entity.CheckConnection;

public class PresenterLogin implements FirebaseAuthenticator.IListener {

    private ILoginView iLoginView;
    private FirebaseAuthenticator authenticator;

    public PresenterLogin(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        authenticator = new FirebaseAuthenticator(this);
    }

    public void login(String email, String password){

        if (CheckConnection.haveNetworkConnection(iLoginView.getContext())) {

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                iLoginView.showLoading(true);
                // presenter se yeu cau authenticator xac thuc, ket qua duoc tra ve qua interface listenner
                authenticator.authenticate(email,password);
            } else {
                iLoginView.showMessage("Hãy Nhập đầy đủ !");
            }

        } else
            iLoginView.showMessage("Kiểm tra lại kết nối internet");
    }


    @Override
    public void onLoginSuccessful() {
        iLoginView.showLoading(false);
        iLoginView.close();

    }

    @Override
    public void onLoginFailed(String error) {
        iLoginView.showMessage(error);
        iLoginView.showLoading(false);
    }
    public interface ILoginView{
        void showMessage(String msg);
        void showLoading(boolean vis);
        Context getContext();
        void close();
    }
}


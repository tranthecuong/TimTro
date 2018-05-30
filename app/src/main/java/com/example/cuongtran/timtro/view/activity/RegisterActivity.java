package com.example.cuongtran.timtro.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.entity.CheckConnection;
import com.example.cuongtran.timtro.presenter.PresenterRegister;

public class RegisterActivity extends AppCompatActivity implements PresenterRegister.IRegisterView {
    EditText editTextHoTen,editTextEmail,editTextPass,editTextRepass;
    Button buttonDangKy;
    ProgressDialog progressDialog;
    Context mContext;
    PresenterRegister presenterRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterRegister= new PresenterRegister(this);
        setContentView(R.layout.activity_register);
        mContext=this;
        editTextHoTen=(EditText)findViewById(R.id.editTexthoTen_DangKy);
        editTextEmail =(EditText)findViewById(R.id.editTextUser_DangKy);
        editTextPass=(EditText)findViewById(R.id.editTextPass_DangKy);
        editTextRepass=(EditText)findViewById(R.id.editTextRePass_DangKy);
        buttonDangKy=(Button)findViewById(R.id.buttonDangKi_DangKy);
        progressDialog  = new ProgressDialog(this);
        buttonDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckConnection.haveNetworkConnection(mContext)){

                    String hoten=editTextHoTen.getText().toString().trim();
                    String user=editTextEmail.getText().toString().trim();
                    String pass=editTextPass.getText().toString().trim();
                    String repass=editTextRepass.getText().toString().trim();
                    // Goi toi presenter
                    presenterRegister.register(user,hoten,pass,repass);


                }else Toast.makeText(mContext, "Kiểm tra lại kết nối internet", Toast.LENGTH_SHORT).show();


            }
        });
    }


    @Override
    public void showMessage(String mess) {
        Toast.makeText(RegisterActivity.this, mess, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showDialog(boolean vis) {
        if(vis) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Signing up...");
            progressDialog.show();
        }else{
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void success() {
        Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        finish();
    }
}

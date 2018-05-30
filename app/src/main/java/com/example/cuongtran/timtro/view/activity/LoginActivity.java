package com.example.cuongtran.timtro.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.presenter.PresenterLogin;

public class LoginActivity extends AppCompatActivity implements PresenterLogin.ILoginView {
    Context mContext = this;
    EditText edtEmail, edtPass;
    Button btnSignUp, btnLogIn;
    ProgressDialog progressDialog;

    private PresenterLogin presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhxa();
        presenter = new PresenterLogin(this);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // yeu cau thang presenter xac thuc
                presenter.login(edtEmail.getText().toString().trim(),edtPass.getText().toString().trim());
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void anhxa() {
        edtEmail = (EditText) findViewById(R.id.edt_email_login);
        edtPass = (EditText) findViewById(R.id.edt_pass_login);
        btnLogIn = (Button) findViewById(R.id.btn_login_login);
        btnSignUp = (Button) findViewById(R.id.btn_sigin_login);

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean vis) {
        if(vis) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Loging in.......");
            progressDialog.setTitle("Please wait.");
            progressDialog.show();
        }else{
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void close() {
        finish();
    }
}

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
import com.example.cuongtran.timtro.presenter.PresenterChinhSuaProfile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChinhSuaProfile extends AppCompatActivity implements PresenterChinhSuaProfile.IView {
    ProgressDialog progressDialog;
    EditText editTen,editSdt,editDiachi;
    Button button;
    String id,ten,sdt,diachi;
    Context mContext;
    PresenterChinhSuaProfile presenterChinhSuaProfile= new PresenterChinhSuaProfile(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_profile);
        mContext=this;
        editTen=(EditText)findViewById(R.id.edit_ten_SuaProfile);
        editSdt=(EditText)findViewById(R.id.edit_sdt_SuaProfile);
        editDiachi=(EditText)findViewById(R.id.edit_diaChi_SuaProfile);
        button=(Button)findViewById(R.id.btn_edit_profile);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            id=bundle.getString("id").toString().trim();
            ten=bundle.getString("ten").toString().trim();
            sdt=bundle.getString("sdt").toString().trim();
            diachi=bundle.getString("diachi").toString().trim();

        }
        editTen.setText(ten);
        if(!sdt.equals("nonono")){
            editSdt.setText(sdt);
        }
        if(!diachi.equals("nonono")){
            editDiachi.setText(diachi);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ten=editTen.getText().toString().trim();
                sdt=editSdt.getText().toString().trim();
                diachi=editDiachi.getText().toString().trim();
                presenterChinhSuaProfile.suaProfile(id,ten,sdt,diachi);

            }
        });
    }



    @Override
    public void showMess(String mess) {
        Toast.makeText(mContext, mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(boolean vis) {
        if(vis==true){
            progressDialog= new ProgressDialog(mContext);
            progressDialog.setTitle("Cập nhật");
            progressDialog.setMessage("Xin chờ");
            progressDialog.show();
        }else {
            progressDialog.dismiss();
            finish();
        }
    }
}

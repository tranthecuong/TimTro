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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChinhSuaProfile extends AppCompatActivity {
    ProgressDialog progressDialog;

    EditText editTen,editSdt,editDiachi;
    Button button;
    String id,ten,sdt,diachi;
    Context mContext;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_profile);
        mContext=this;
        firebaseFirestore=FirebaseFirestore.getInstance();
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
                if(ten.isEmpty()||sdt.isEmpty()||diachi.isEmpty()){
                    Toast.makeText(mContext, "Các trường không được để trống ! ", Toast.LENGTH_SHORT).show();
                }
                else {
                    suaProfile();
                }

            }
        });
    }

    private void suaProfile() {
        progressDialog= new ProgressDialog(mContext);
        progressDialog.setTitle("Cập nhật");
        progressDialog.setMessage("Xin chờ");
        progressDialog.show();
        firebaseFirestore.collection("taikhoan").document(id)
                .update("hoten",ten).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                firebaseFirestore.collection("taikhoan").document(id)
                        .update("sodienthoai",sdt).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseFirestore.collection("taikhoan").document(id)
                                .update("diachi",diachi).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        });


                    }
                });

            }
        });



    }
}

package com.example.cuongtran.timtro.model.data;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModelChinhSuaProfile {
    ICallBack iCallBack;
    FirebaseFirestore firebaseFirestore;
    public ModelChinhSuaProfile(ICallBack iCallBack){
        this.iCallBack=iCallBack;
        firebaseFirestore=FirebaseFirestore.getInstance();
    }
    public  interface ICallBack{
        void onSuccess(String mess);

    }
    public void suaProfile(final String id, String ten, final String sdt, final String diachi) {

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
                               iCallBack.onSuccess("Cập nhật thành công");
                                //Toast.makeText(mContext, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

            }
        });



    }
}

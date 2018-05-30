package com.example.cuongtran.timtro.model.data;

import android.support.annotation.NonNull;
import android.view.View;
import com.example.cuongtran.timtro.model.authenticate.FirebaseAuthenticator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ModelDangKyThongBao {
    FirebaseFirestore firebaseFirestore;
    IListener iListener;
    FirebaseAuthenticator firebaseAuthenticator;
    public ModelDangKyThongBao(IListener iListener){
        this.iListener=iListener;
        firebaseAuthenticator= new FirebaseAuthenticator();
        firebaseFirestore= FirebaseFirestore.getInstance();
    }
    public void turnOffNotification(){
        firebaseFirestore.collection("taikhoan").document(firebaseAuthenticator.getUserId()).update("dangki", "false").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                iListener.showMess("Đã tắt thông báo");
            }
        });

    };
    public  void turnOnNotification(ArrayList<String> arr){
        firebaseFirestore.collection("taikhoan").document(firebaseAuthenticator.getUserId())
                .update("dangki", "true", "tieuchi", arr)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        iListener.showMess("Nhận thông báo thành công");
                    }
                });

    }
    public  void checkTrangThai(){

        firebaseFirestore.collection("taikhoan").document(firebaseAuthenticator.getUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String dk = document.getString("dangki");
                        if (dk.equals("true")) {
                            iListener.setVisibility(View.VISIBLE);
                        } else {
                            iListener.setVisibility(View.GONE);
                        }
                    }
                } else {
                    iListener.showMess("Loi get trang thai manhinh3");
                }

            }
        });

    }

    public interface IListener{
        void showMess(String mess);
        void setVisibility(int i);

    }
}

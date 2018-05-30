package com.example.cuongtran.timtro.model.data;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModelRegister {
    FirebaseAuth mAuth;
    FirebaseFirestore db ;
    Ipresenter ipresenter;
    public ModelRegister(Ipresenter ipresenter){
        this.ipresenter=ipresenter;
        mAuth=FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
    }

    public void signup(final String hoten, final String email, String pass){
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    // neu dang ki thanh cong
                    FirebaseUser user = mAuth.getCurrentUser();
                    String id =user.getUid();
                    Map<String,Object> data = new HashMap<>();
                    data.put("linkavatar","nonono");
                    data.put("idtaikhoan",id);
                    data.put("hoten",hoten);
                    data.put("email",email);
                    data.put("diachi","nonono");
                    data.put("sodienthoai","nonono");
                    data.put("dangki","false");
                    db.collection("taikhoan").document(id).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                saveTokenToDB();
                            }else {
                                //Toast.makeText(mContext,task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                ipresenter.onError(task.getException().getMessage().toString());
                            }

                        }
                    });



                }
                else {

                    //progressDialog.dismiss();
                    // neu that bai
                    String error= task.getException().getMessage().toString();
                    ipresenter.onError(error);
                    //Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();

                }

            }
        }) ;

    }

    public interface Ipresenter{
        void onSuccess();
        void onError(String mess);

    }


    private void saveTokenToDB() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> data = new HashMap<>();
        data.put("token", Arrays.asList(refreshedToken));
        FirebaseFirestore.getInstance()
                .collection("taikhoan")
                .document(id)
                .set(data, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ipresenter.onSuccess();
                        } else {
                            Log.e("CUONG", task.getException().getMessage().toString());
                        }
                    }
                });
    }
}

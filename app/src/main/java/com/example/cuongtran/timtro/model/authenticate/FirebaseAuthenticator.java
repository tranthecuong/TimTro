package com.example.cuongtran.timtro.model.authenticate;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseAuthenticator {
    private FirebaseAuth mAuth;
    IListener iListener;

    public FirebaseAuthenticator(IListener iListener){
        this.iListener=iListener;
    }
    public FirebaseAuthenticator(){

    }

    public boolean authenticate(String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkAndSaveTokenToDB();
                    //iListener.onLoginSuccessful();
                } else {
                    String error = task.getException().getMessage();
                    iListener.onLoginFailed(error);
                }
            }
        });
        return false;
    }

    public boolean checkLogon() {
        mAuth=FirebaseAuth.getInstance();
        return mAuth.getCurrentUser()!=null;
    }

    public String getUserId() {
        mAuth=FirebaseAuth.getInstance();
        return mAuth.getCurrentUser()!=null?mAuth.getCurrentUser().getUid():null;
    }

    private void checkAndSaveTokenToDB() {
        final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // lay ve token cho vao mang
        FirebaseFirestore.getInstance()
                .collection("taikhoan")
                .document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String,Object> map= document.getData();
                    ArrayList<String> arr =(ArrayList<String>) map.get("token");
                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    // kiem tra da co refresh token chua
                    int check =0;
                    for(String s:arr){
                        if(s.equals(refreshedToken)){
                            check=1;
                            Log.e("CUONG","da co token roi");
                            break;
                        }
                    }

                    if(check==0){
                        Log.e("CUONG","chua co token, them vao DB");
                        arr.add(refreshedToken);

                        Map<String, Object> data = new HashMap<>();
                        data.put("token", arr);

                        FirebaseFirestore.getInstance()
                                .collection("taikhoan")
                                .document(id)
                                .set(data, SetOptions.merge())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
//                                            progressDialog.dismiss();
//                                            finish();
                                            iListener.onLoginSuccessful();
                                        } else {
                                            Log.e("CUONG", task.getException().getMessage().toString());
                                        }
                                    }
                                });


                    }
                    else {
                        iListener.onLoginSuccessful();
//                        progressDialog.dismiss();
//                        finish();
                    }


                } else {
                    Log.e("CUONG", "No such document");
                }
            }
        });


    }

    public interface IListener {
        void onLoginSuccessful();
        void onLoginFailed(String error);
    }
}

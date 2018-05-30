package com.example.cuongtran.timtro.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{

    //FirebaseFirestore db = FirebaseFirestore.getInstance();
    //FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //sendRegistrationToServer();
    }

//    private void sendRegistrationToServer() {
//        if (auth.getCurrentUser() != null) {
//            final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//            Log.e("CUONG"," onToken Refresh vao trong roi");
//
//            String id = auth.getCurrentUser().getUid();
//            Map<String, Object> data = new HashMap<>();
//            data.put("token", Arrays.asList(refreshedToken));
//
//            db.collection("taikhoan").document(id).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        Log.e("CUONG", "token " + refreshedToken);
//                    } else {
//                        Log.e("CUONG", task.getException().getMessage().toString());
//                    }
//                }
//            });
//        }
//    }
}

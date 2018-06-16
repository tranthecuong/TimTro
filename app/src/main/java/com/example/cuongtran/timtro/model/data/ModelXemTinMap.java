package com.example.cuongtran.timtro.model.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.cuongtran.timtro.entity.ThoiGian;
import com.example.cuongtran.timtro.entity.TinDang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ModelXemTinMap {
    IListener iListener;
    FirebaseFirestore db;
    ArrayList<TinDang> arrTinDang;
    public ModelXemTinMap(IListener iListener){
        this.iListener=iListener;
        arrTinDang= new ArrayList<>();
        db=FirebaseFirestore.getInstance();
    }
    public interface IListener{
        void getDataSuccess(ArrayList<TinDang> arr);

    }
    public void getData(){
        arrTinDang.clear();
        Map<String,Object> data = new HashMap<>();
        data.put("currentime", FieldValue.serverTimestamp());
        // lay ve timestamp tren fireBase
        db.collection("time").document("currentime").set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.collection("time").document("currentime").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Timestamp timestamp=documentSnapshot.getTimestamp("currentime");
                        Date currentTime1 =timestamp.toDate();
                        Log.e("CUONG","time"+ currentTime1);
                        // lay ve cac tin trong 30 ngay gan nhat
                        //Date currentTime1 = Calendar.getInstance().getTime();
                        Calendar c = Calendar.getInstance();
                        c.setTime(currentTime1);
                        c.add(Calendar.DATE, -30);
                        Date time30 = c.getTime();
                        Log.e("CUONG","time after "+ time30);
                        Timestamp timestamp2 = new Timestamp(time30);

                        db.collection("nhatro")
                                .whereGreaterThanOrEqualTo("ngaydang", timestamp2)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                TinDang tinDang = null;
                                                tinDang = document.toObject(TinDang.class).withId(document.getId());
                                                arrTinDang.add(tinDang);
                                            }
                                            iListener.getDataSuccess(arrTinDang);

                                        }

                                    }
                                });


                    }
                });
            }
        });
    }
}

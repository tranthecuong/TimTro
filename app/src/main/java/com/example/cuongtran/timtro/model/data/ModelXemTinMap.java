package com.example.cuongtran.timtro.model.data;

import android.support.annotation.NonNull;

import com.example.cuongtran.timtro.entity.TinDang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ModelXemTinMap {
    IListener iListener;
    FirebaseFirestore db;
    ArrayList<TinDang> arrTinDang;
    public ModelXemTinMap(IListener iListener){
        this.iListener=iListener;
        arrTinDang= new ArrayList<>();
    }
    public interface IListener{
        void getDataSuccess(ArrayList<TinDang> arr);

    }
    public void getData(){

        db = FirebaseFirestore.getInstance();
        db.collection("nhatro")
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
}

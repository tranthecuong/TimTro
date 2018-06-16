package com.example.cuongtran.timtro.model.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.cuongtran.timtro.entity.Constant;
import com.example.cuongtran.timtro.entity.TinDang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ModelLocTin {
    ICallback iCallback;
    FirebaseFirestore db;
    ArrayList<TinDang> arrayList;
    public ModelLocTin(ICallback iCallback){
        this.iCallback=iCallback;
        db=FirebaseFirestore.getInstance();
        arrayList= new ArrayList<>();
    }

    public void truyVan1(long mingia, long maxgia, final long minS, final long maxS) {
        Query query = null;
        query= db.collection(Constant.NHA_TRO)
                .whereGreaterThanOrEqualTo("gia",mingia)
                .whereLessThanOrEqualTo("gia",maxgia);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    arrayList.clear();
                    TinDang tinDang = null;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        tinDang = doc.toObject(TinDang.class).withId(doc.getId());
                        long s=tinDang.getDientich();
                        if((s>=minS)&&(s<=maxS)){
                            arrayList.add(tinDang);
                        }

                    }
                    iCallback.truyVanSuccess(arrayList);


                }
                else {
                    Log.e("CUONG", "Error getting documents: ", task.getException());

                }
            }
        });
    }

    public void truyVan2(String huyen, long mingia, long maxgia, final long minS, final long maxS) {
        Query query = null;
        query= db.collection(Constant.NHA_TRO)
                .whereEqualTo("tenquanhuyen",huyen)
                .whereGreaterThanOrEqualTo("gia",mingia)
                .whereLessThanOrEqualTo("gia",maxgia);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    arrayList.clear();
                    TinDang tinDang = null;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        tinDang = doc.toObject(TinDang.class).withId(doc.getId());
                        long s=tinDang.getDientich();
                        if((s>=minS)&&(s<=maxS)){
                            arrayList.add(tinDang);
                        }
                    }
                   iCallback.truyVanSuccess(arrayList);

                }
                else {
                    Log.e("CUONG", "Error getting documents: ", task.getException());

                }
            }
        });
    }

    public void truyVan3(String tinh, long mingia, long maxgia, final long minS, final long maxS) {
        Query query = null;
        query= db.collection(Constant.NHA_TRO)
                .whereEqualTo("tenthanhpho",tinh)
                .whereGreaterThanOrEqualTo("gia",mingia)
                .whereLessThanOrEqualTo("gia",maxgia);


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    arrayList.clear();
                    TinDang tinDang = null;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        tinDang = doc.toObject(TinDang.class).withId(doc.getId());
                        long s=tinDang.getDientich();
                        if((s>=minS)&&(s<=maxS)){
                            arrayList.add(tinDang);
                        }
                    }
                    iCallback.truyVanSuccess(arrayList);

                }
                else {
                    Log.e("CUONG", "Error getting documents: ", task.getException());

                }
            }
        });
    }

    public void truyVan4(final String tinh, String huyen, long mingia, long maxgia, final long minS, final long maxS) {
        Query query = null;
        query= db.collection(Constant.NHA_TRO)
                .whereEqualTo("tenquanhuyen",huyen)
                .whereGreaterThanOrEqualTo("gia",mingia)
                .whereLessThanOrEqualTo("gia",maxgia);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    arrayList.clear();
                    TinDang tinDang = null;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        tinDang = doc.toObject(TinDang.class).withId(doc.getId());
                        long s=tinDang.getDientich();
                        String h=tinDang.gettenthanhpho();
                        if((s>=minS)&&(s<=maxS)&&(h.equals(tinh))){
                            arrayList.add(tinDang);
                        }
                    }
                   iCallback.truyVanSuccess(arrayList);
                }
                else {
                    Log.e("CUONG", "Error getting documents: ", task.getException());

                }
            }
        });
    }


    public interface ICallback{

        void truyVanSuccess(ArrayList<TinDang> arrayList);
    }
}

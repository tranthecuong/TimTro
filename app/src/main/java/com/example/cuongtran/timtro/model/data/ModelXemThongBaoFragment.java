package com.example.cuongtran.timtro.model.data;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.cuongtran.timtro.entity.ThongBao;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ModelXemThongBaoFragment {
    ICallBack iCallBack;
    FirebaseFirestore firebaseFirestore;
    List<ThongBao> arrayList;
    public  ModelXemThongBaoFragment(ICallBack iCallBack){
        this.iCallBack=iCallBack;
        arrayList = new ArrayList<>();
    }

    public void getData(String idCurrentUser) {
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("thongbao")
                .whereEqualTo("idUser", idCurrentUser)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("CUONG", "Listen failed.", e);
                            return;
                        }

                        //List<ThongBao> listThongBao = new ArrayList<>();
                        arrayList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            ThongBao t = doc.toObject(ThongBao.class);
                            arrayList.add(t);
                        }
                        iCallBack.getDataSuccess(arrayList);
                        //Log.e("CUONG", "List Thong bao " + arrayList.get(0).getIdNhaTro());
                    }
                });

    }

    public interface ICallBack{
        void getDataSuccess(List<ThongBao> arrayList);
    }
}

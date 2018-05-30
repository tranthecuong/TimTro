package com.example.cuongtran.timtro.model.data;

import android.support.annotation.NonNull;

import com.example.cuongtran.timtro.entity.TinDang;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModelChiTietBanDo {
    Icallback icallback;
    FirebaseFirestore db;
    String kinhdo, vido;
    String diaChi;
    public ModelChiTietBanDo(Icallback icallback){
        this.icallback=icallback;
        db=FirebaseFirestore.getInstance();
    }

    public void getData(String idNhanDuoc){
        db.collection("nhatro").document(idNhanDuoc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                TinDang ob =task.getResult().toObject(TinDang.class);
                diaChi=ob.getChitietdiachi()+", "+ob.gettenquanhuyen()+", "+ob.gettenthanhpho();
                kinhdo=ob.getKinhdo();
                vido=ob.getVido();
                icallback.result(diaChi,kinhdo,vido);

            }
        });


    };
    public interface Icallback{
        void result(String diaChi,String kinhdo,String vido);

    }
}

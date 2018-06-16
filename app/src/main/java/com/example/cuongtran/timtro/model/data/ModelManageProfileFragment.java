package com.example.cuongtran.timtro.model.data;

import android.support.annotation.NonNull;

import com.example.cuongtran.timtro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class ModelManageProfileFragment {
    ICallBack iCallBack;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    public ModelManageProfileFragment(ICallBack iCallBack){
        this.iCallBack=iCallBack;
        db=FirebaseFirestore.getInstance();
    }

    public void getData(String id) {
        collectionReference=db.collection("taikhoan");
        collectionReference.whereEqualTo("idtaikhoan", id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //txtvTenTK.setText(document.getString("hoten"));
                        String name=document.getString("hoten");
                        String link =document.getString("linkavatar");
                        iCallBack.returnData(name,link);

                    }
                }

            }
        });
    }

    public interface ICallBack{
        void returnData(String name,String url);
    }
}

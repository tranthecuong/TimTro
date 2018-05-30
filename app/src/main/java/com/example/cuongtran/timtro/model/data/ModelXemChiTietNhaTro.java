package com.example.cuongtran.timtro.model.data;


import android.support.annotation.NonNull;
import android.util.Log;
import com.example.cuongtran.timtro.entity.Chat;
import com.example.cuongtran.timtro.entity.Constant;
import com.example.cuongtran.timtro.entity.TinDang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModelXemChiTietNhaTro {
    ICallBack icallback;
    ArrayList<Chat> arrChat ;
    private FirebaseFirestore db;
    TinDang tinDang;
    String link,userName;

    public ModelXemChiTietNhaTro(ICallBack icallback){
        this.icallback=icallback;
        arrChat = new ArrayList<>();
        db=FirebaseFirestore.getInstance();
    }


    public interface ICallBack{
        void returnData(TinDang tinDang,String urlAvatar,String nameUser,ArrayList<Chat> chats);
        void showMess(String mess);
        void dissmissDialog();
    }

    public void addChatToFireBase(String tentk,String text,String idNhanDuoc){
        Map<String,Object> data= new HashMap<>();
        data.put("user",tentk);
        data.put("text",text);
        data.put("timestamp", FieldValue.serverTimestamp());

        db.collection(Constant.NHA_TRO).document(idNhanDuoc).collection("chat")
                .add(data);

    };

    public void getData(String idNha) {

        //get chat ve
        db.collection("nhatro").document(idNha).collection("chat")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Chat chat =document.toObject(Chat.class);
                                arrChat.add(chat);
                                //Log.e("CUONG", document.getId() + " => " + document.getData()+" "+arrChat.size());

                            }
                            //icallback.returnChat(arrChat);

                        } else {
                            Log.e("CUONG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        db.collection("nhatro").document(idNha).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // lay duoc tin dang
                        tinDang =document.toObject(TinDang.class);
                       // lay ve thong tin user
                        String idtk =tinDang.getIdtaikhoan();
                        DocumentReference docRef = db.collection("taikhoan").document(idtk);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        link =document.getString("linkavatar");
                                        userName=document.getString("hoten");
                                        icallback.returnData(tinDang,link,userName,arrChat);
                                    }
                                }
                            }
                        });


                    } else {
                        icallback.showMess("Tin đã bị xóa");
                        icallback.dissmissDialog();
//                        Toast.makeText(context, "Tin đã bị xóa", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                } else {
                    Log.d("CUONG", "get failed with ", task.getException());
                }


            }
        });


    }

    public void luuTin(final String idTin, final String idTK) {
        // check da co tin tren fireStore chua
        db.collection(Constant.TIN_LUU).whereEqualTo(Constant.TIN_LUU_IDTIN,idTin)
                .whereEqualTo(Constant.TIN_LUU_IDTK,idTK)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().isEmpty()){
                    // them vao DB
                    Map<String,String> params = new HashMap<>();
                    params.put(Constant.TIN_LUU_IDTK,idTK);
                    params.put(Constant.TIN_LUU_IDTIN,idTin);
                    db.collection(Constant.TIN_LUU).add(params).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Log.e("CUONG","Luu Thanh Cong");
                        }
                    });

                }
                else {
                    Log.e("CUONG","Da luu roi");
                }
            }
        });

    }
}

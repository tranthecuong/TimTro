package com.example.cuongtran.timtro.model.data;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ModelDangTin {
    ICallBack iCallBack;
    FirebaseUser mCurrentUser;
    FirebaseFirestore db;
    FirebaseStorage mStorage;
    StorageReference storageReference;
    String idTaiKhoan;
    public ModelDangTin(ICallBack iCallBack){
        this.iCallBack=iCallBack;
        db=FirebaseFirestore.getInstance();
        mStorage=FirebaseStorage.getInstance();
        storageReference=mStorage.getReference();
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    public interface ICallBack{
        void onFail(String mess);
        void onSuccess(String mess);
    }

    public void dangTin(Bitmap resizedBitmap, final String idThanhPho, final String idQuanHuyen, final String chiTietDiaChi, final String gia, final String dienTich,
                        final String dienThoai, final String moTa, final LatLng mLatLng) {
        //Dua anh vao Storage
        Calendar calendar= Calendar.getInstance();
        StorageReference img_ref= storageReference.child("anhnhatro").child("nhatro"+ calendar.getTimeInMillis()+".PNG");
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte data2[]=baos.toByteArray();
        UploadTask uploadTask= img_ref.putBytes(data2);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iCallBack.onFail(e.getMessage().toString());
                //progressDialog.dismiss();
                //Toast.makeText(mContext, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url=taskSnapshot.getDownloadUrl().toString();
                idTaiKhoan= mCurrentUser.getUid();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("tenthanhpho", idThanhPho);
                params.put("tenquanhuyen", idQuanHuyen);
                params.put("chitietdiachi", chiTietDiaChi);
                params.put("gia", Long.parseLong(gia));
                params.put("dientich", Long.parseLong(dienTich));
                params.put("dienthoai", dienThoai);
                params.put("mota", moTa);
                params.put("idtaikhoan", idTaiKhoan);
                params.put("ngaydang", FieldValue.serverTimestamp());
                params.put("anh",url);
                String kinhdo=String.valueOf(mLatLng.longitude);
                String vido = String.valueOf(mLatLng.latitude);
                params.put("kinhdo",kinhdo);
                params.put("vido",vido);
                db.collection("nhatro").add(params).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        iCallBack.onSuccess("Đăng tin thành công !");
                        //Toast.makeText(mContext, "Đăng tin thành công !", Toast.LENGTH_SHORT).show();
                        //progressDialog.dismiss();
                        //finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iCallBack.onFail(e.getMessage().toString());
                        //Toast.makeText(mContext, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                       // progressDialog.dismiss();
                    }
                });

            }
        });

    }
}

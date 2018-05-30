package com.example.cuongtran.timtro.view.adapter;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuongtran.timtro.view.activity.LoginActivity;
import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.entity.CheckConnection;
import com.example.cuongtran.timtro.entity.TinDang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListviewAdapter extends BaseAdapter {
    Context context;
    List<TinDang> arrNhaTro;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    ProgressDialog progressDialog;
    public ListviewAdapter(Context context, List<TinDang> arrNhaTro) {
        this.context = context;
        this.arrNhaTro = arrNhaTro;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public int getCount() {
        return arrNhaTro.size();
    }

    @Override
    public Object getItem(int i) {
        return arrNhaTro.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public  class ViewHolder{
        public TextView textViewNguoiDang, textViewNgayDang,textViewDiaChi,textViewGia,textViewDienTich;
        public ImageView imageViewNhaTro,imgAvatar;
        public Button buttonLuu;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        firebaseFirestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        if(view==null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.customitem, null);
            viewHolder.textViewNguoiDang=(TextView)view.findViewById(R.id.textViewNguoiDang);
            viewHolder.textViewNgayDang=(TextView)view.findViewById(R.id.textViewNgayDang);
            viewHolder.textViewDiaChi=(TextView)view.findViewById(R.id.textViewDiaChi);
            viewHolder.textViewDienTich=(TextView)view.findViewById(R.id.textViewDienTich);
            viewHolder.textViewGia=(TextView)view.findViewById(R.id.textViewGia);
            viewHolder.imageViewNhaTro=(ImageView)view.findViewById(R.id.imageViewNhaTro);
            viewHolder.imgAvatar=(ImageView)view.findViewById(R.id.imageViewNguoiDang);
            viewHolder.buttonLuu=(Button)view.findViewById(R.id.buttonLuu);
            view.setTag(viewHolder);


        }
        else
        {
            viewHolder= (ViewHolder) view.getTag();
        }

        final TinDang tinDang =arrNhaTro.get(i);
        String idtk =tinDang.getIdtaikhoan();
        DocumentReference docRef = firebaseFirestore.collection("taikhoan").document(idtk);
        final ViewHolder finalViewHolder1 = viewHolder;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String link =document.getString("linkavatar");
                        if(!link.equals("nonono")){
                            Picasso.get().load(link)
                                    .into(finalViewHolder1.imgAvatar);

                        }

                        String tentk =document.getString("hoten");
                        finalViewHolder1.textViewNguoiDang.setText(tentk);



                    }
                } else {

                }
            }
        });

        viewHolder.textViewNgayDang.setText(String.valueOf(tinDang.getNgaydang()));
        viewHolder.textViewDiaChi.setMaxLines(2);
        viewHolder.textViewDiaChi.setEllipsize(TextUtils.TruncateAt.END);
        String dc=tinDang.getChitietdiachi()+", "+tinDang.gettenquanhuyen()+", "+tinDang.gettenthanhpho();
        viewHolder.textViewDiaChi.setText("Địa chỉ: "+dc);
        viewHolder.textViewGia.setText("Giá phòng: "+String.valueOf(tinDang.getGia())+" Đ");
        viewHolder.textViewDienTich.setText("Diện tích: "+String.valueOf(tinDang.getDientich())+" mét vuông");
        String url = tinDang.getAnh();
        Picasso.get().load(url)
                .placeholder(R.drawable.ic_photo_camera_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(viewHolder.imageViewNhaTro);

        viewHolder.buttonLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckConnection.haveNetworkConnection(context)){

                    if (mCurrentUser != null) {
                        String idTin = tinDang.id;
                        luuTin(mCurrentUser.getUid(), idTin);
                        Log.e("CUONG", "dataLuu" + mCurrentUser.getUid() + " " + idTin);

                    } else {
                        //yeu cau dang nhap

                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog_Alert));
                        builder.setMessage("Bạn cần đăng nhập trước !")
                                .setCancelable(true)
                                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();

                                    }
                                })
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                        dialogInterface.cancel();

                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Chú ý");
                        alertDialog.show();

                    }
            }
            else Toast.makeText(context, "Kiểm tra lại kết nối", Toast.LENGTH_SHORT).show();



            }
        });

        return view;

    }

    private void luuTin(String uid, String idTin) {
        progressDialog= new ProgressDialog(context);
        progressDialog.setTitle("Lưu Tin");
        progressDialog.setMessage("Đang Lưu......");
        progressDialog.show();
        Map<String,String> params = new HashMap<String, String>();
        //params.put("idtaikhoan",uid);
        params.put("idtin",idTin);
        params.put("idtaikhoan",uid);

        firebaseFirestore.collection("tinluu").add(params).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "Lưu tin thành công", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }


}

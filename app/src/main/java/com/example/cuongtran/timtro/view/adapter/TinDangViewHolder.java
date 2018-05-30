package com.example.cuongtran.timtro.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.entity.TinDang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TinDangViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewNguoiDang)
    public ImageView imgAvatar;

    @BindView(R.id.textViewNguoiDang)
    public TextView textViewNguoiDang;

    @BindView(R.id.textViewNgayDang)
    public TextView textViewNgayDang;

    @BindView(R.id.textViewDiaChi)
    public TextView textViewDiaChi;

    @BindView(R.id.textViewGia)
    public TextView textViewGia;

    @BindView(R.id.textViewDienTich)
    public TextView textViewDienTich;

    @BindView(R.id.imageViewNhaTro)
    public ImageView imageViewNhaTro;

    @BindView(R.id.buttonLuu)
    public Button buttonLuu;

    public TinDangViewHolder(View itemView, final OnClickViewHolderListener onClick) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClickItem(getAdapterPosition());
            }
        });

        buttonLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClickButton(getAdapterPosition());
            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClickAvater(getAdapterPosition());
            }
        });
    }

    public void bindData(TinDang tinDang) {
        // lay avatar
        //lay ten nguoidang
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        String idtk =tinDang.getIdtaikhoan();
        DocumentReference docRef = firebaseFirestore.collection("taikhoan").document(idtk);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String link =document.getString("linkavatar");
                        if(!link.equals("nonono")){
                            Picasso.get().load(link)
                                    .into(imgAvatar);

                        }

                        String tentk =document.getString("hoten");
                        textViewNguoiDang.setText(tentk);

                    }
                } else {

                }
            }
        });

        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date=tinDang.getNgaydang().toDate();
        String ngay= sfd.format(date);
        textViewNgayDang.setText(ngay);
        String dc = tinDang.getChitietdiachi() + ", " + tinDang.gettenquanhuyen() + ", " + tinDang.gettenthanhpho();
        textViewDiaChi.setText("Địa chỉ: " + dc);
        textViewGia.setText("Giá phòng: " + String.valueOf(tinDang.getGia()) + " Đ");
        textViewDienTich.setText("Diện tích: " + String.valueOf(tinDang.getDientich()) + " mét vuông");
        String url = tinDang.getAnh();
        Picasso.get()
                .load(url)
                //.placeholder(R.drawable.ic_photo_camera_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(imageViewNhaTro);
    }

    public interface OnClickViewHolderListener{
        public void onClickItem(int position);
        public void onClickButton(int Position);
        public void onClickAvater(int Position);

    }
}

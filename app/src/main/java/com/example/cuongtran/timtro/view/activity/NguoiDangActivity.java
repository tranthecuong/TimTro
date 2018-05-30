package com.example.cuongtran.timtro.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.entity.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NguoiDangActivity extends AppCompatActivity {
    String idNhanDuoc;
    FirebaseFirestore firebaseFirestore;
    @BindView(R.id.img_avatar_nguoidang)
    ImageView img;
    @BindView(R.id.txtv_nguoidang)
    TextView txtvNguoiDang;
    @BindView(R.id.sdt_nguoidang)
    TextView txtvSdt;
    @BindView(R.id.email_nguoidang)
    TextView txtvEmail;
    @BindView(R.id.diaChi_nguoidang)
    TextView txtvDiaChi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoi_dang);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idNhanDuoc = extras.getString("TAIKHOAN").toString().trim();
            Log.e("CUONG","nhan duoc"+ idNhanDuoc);
            getData();
        }
    }

    private void getData() {
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constant.TAI_KHOAN).document(idNhanDuoc).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot=task.getResult();
                            String link =snapshot.getString("linkavatar");
                            String ten=snapshot.getString("hoten");
                            String sdt=snapshot.getString("sodienthoai") ;
                            String diachi=snapshot.getString("diachi");
                            String email=snapshot.getString("email");
                            if(!link.equals("nonono")){
                                Picasso.get().load(link)
                                        .into(img);
                            }
                            if(!sdt.equals("nonono")){
                                txtvSdt.setText(sdt);
                            }
                            if(!diachi.equals("nonono")){
                                txtvDiaChi.setText(diachi);
                            }
                            txtvNguoiDang.setText(ten);
                            txtvEmail.setText(email);
                        }

                    }
                });

    }
}

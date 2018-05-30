package com.example.cuongtran.timtro.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.entity.Constant;
import com.example.cuongtran.timtro.entity.ThongBao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterManHinh4 extends BaseAdapter {
    Context context;
    int layout;
    List<ThongBao> arrTin ;

    public AdapterManHinh4(Context context, int layout, List<ThongBao> arrTin) {
        this.context = context;
        this.layout = layout;
        this.arrTin = arrTin;
    }
    @Override
    public int getCount() {
        return arrTin.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view=layoutInflater.inflate(layout,null);
        final ImageView imageView =(ImageView)view.findViewById(R.id.img_itemTab4);
        final TextView txtvThongBao=(TextView)view.findViewById(R.id.tv_thongbao_item_Tab4);
        TextView txtvNgayThongBao=(TextView)view.findViewById(R.id.tv_Ngay_itemTab4);

        String idTin = arrTin.get(i).getIdNhaTro();
        long time = arrTin.get(i).getCreated();
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date=new Date(time);
        String ngay= sfd.format(date);
        txtvNgayThongBao.setText(ngay);
        FirebaseFirestore.getInstance().collection(Constant.NHA_TRO).document(idTin)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot=task.getResult();
                if(documentSnapshot.exists()){
                    String taikhoanDang = documentSnapshot.getString("idtaikhoan");

                    FirebaseFirestore.getInstance().collection(Constant.TAI_KHOAN).document(taikhoanDang)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot=task.getResult();
                            if(documentSnapshot.exists()){
                                String link =documentSnapshot.getString("linkavatar");
                                if(!link.equals("nonono")){
                                    Picasso.get().load(link)
                                            .into(imageView);

                                }
                                String tentk =documentSnapshot.getString("hoten");
                                txtvThongBao.setText(tentk+" đã đăng nhà trọ thỏa mãn");
                            }
                        }
                    });



                }
            }
        });







        return view;
    }
}

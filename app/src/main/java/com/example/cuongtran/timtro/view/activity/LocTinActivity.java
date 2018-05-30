package com.example.cuongtran.timtro.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.view.adapter.AdapterTabHost;
import com.example.cuongtran.timtro.entity.Constant;
import com.example.cuongtran.timtro.entity.TinDang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LocTinActivity extends AppCompatActivity {
    CrystalRangeSeekbar Seekbargia,Seekbardientich;
    Spinner spinner1,spinner2;
    String[] dataSpinner1, dataSpinner2, dataSpinner22;
    TextView txtvGia, txtvDienTich;
    Button btnApply;
    Float a,b;
    Long a2,b2;
    Context mContext;
    ListView listView;
    ArrayList<TinDang> arrayList= new ArrayList<>();
    AdapterTabHost adapterTabHost;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_tin);
        mContext=this;
        db=FirebaseFirestore.getInstance();
        listView=(ListView)findViewById(R.id.lv_loctin);
        adapterTabHost= new AdapterTabHost(this,R.layout.itemtabhost,arrayList);
        listView.setAdapter(adapterTabHost);
        Seekbargia=(CrystalRangeSeekbar)findViewById(R.id.seek_bar_gia);
        Seekbardientich=(CrystalRangeSeekbar)findViewById(R.id.seek_bar_dientich);
        txtvGia=(TextView)findViewById(R.id.mingia);
        txtvDienTich=(TextView)findViewById(R.id.mindientich);
        btnApply=(Button)findViewById(R.id.btn_apply_filter) ;
        spinner1=(Spinner)findViewById(R.id.spinnerTinh_LocTin);
        spinner2=(Spinner)findViewById(R.id.spinnerQuan_LocTin);
        dataSpinner1 = new String[]{"Tất cả","Hà Nội", "TP Hồ Chí Minh",};
        dataSpinner2 = new String[]{"Tất cả","Quận Ba Đình", "Quận Hoàn Kiếm", "Quận Hai Bà Trưng",
                "Quận Đống Đa", "Quận Tây Hồ", "Quận Cầu Giấy", "Quận Thanh Xuân", "Quận Hoàng Mai",
                "Quận Long Biên", "Quận Bắc Từ Liêm", "Huyện Thanh Trì", "Huyện Gia Lâm", "Huyện Đông Anh",
                "Huyện Sóc Sơn", "Quận Hà Đông", "Thị Xã Sơn Tây",
                "Huyện Ba Vì", "Huyện Phúc Thọ", "Huyện Thạch Thất", "Huyện Quốc Oai",
                "Huyện Chương Mỹ", "Huyện Đan Phượng", "Huyện Hoài Đức", "Huyện Thanh Oai", "Huyện Mỹ Đức",
                "Huyện Ứng Hòa", "Huyện Thường Tín", "Huyện Phú Xuyên", "Huyện Mê Linh", "Quận Nam Từ Liêm"};
        dataSpinner22 = new String[]{"Tất cả","Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6",
                "Quận 7", "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12", "Quận Gò Vấp", "Quận Tân Bình", "Quận Tân Phú", "Quận Bình Thạnh",
                "Quận Phú Nhuận", "Quận Thủ Đức", "Quận BÌnh Tân", "Huyện Bình Chánh", "Huyện Củ Chi",
                "Huyện Hóc Môn", "Huyện Nhà Bè", "Huyện Cần Giờ"};
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataSpinner1);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adp1);

        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataSpinner2);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adp2);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(mContext, String.valueOf(i), Toast.LENGTH_SHORT).show();
                if (i == 1) {
                    ArrayAdapter<String> adp2 = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_spinner_item, dataSpinner2);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adp2);

                } else if (i == 2) {
                    ArrayAdapter<String> adp2 = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_spinner_item, dataSpinner22);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adp2);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // set listener
        Seekbargia.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                DecimalFormat df = new DecimalFormat("#.0");
                 a = Float.valueOf(df.format(minValue));
                 b = Float.valueOf(df.format(maxValue));
                if((a==0.0)&&(b==15.0)){
                    txtvGia.setText("Tất cả");
                }
                else {
                    txtvGia.setText("từ "+String.valueOf(a)+" triệu - "+String.valueOf(b)+" triệu" );

                }


            }
        });
        Seekbardientich.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                a2=(Long) minValue;
                b2=(Long)maxValue;
                if((a2==0)&&(b2==100)){
                    txtvDienTich.setText("Tất cả");
                }
                else {
                    txtvDienTich.setText("từ "+String.valueOf(a2)+" mét vuông - "+String.valueOf(b2)+" mét vuông" );

                }

            }
        });

        Seekbargia.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                //Log.e("CUONG", String.valueOf(a) + " : " + String.valueOf(b));
            }
        });
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tinh = spinner1.getSelectedItem().toString();
                String huyen =spinner2.getSelectedItem().toString();
                long t1= (long) (a*1000000);
                long t2= (long) (b*1000000);
                updateFilter(tinh,huyen,t1,t2,a2,b2);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(getApplicationContext(),XemChiTietNhaTroActivity.class);
                intent.putExtra("CUONG",arrayList.get(i).id);
                startActivity(intent);
            }
        });

    }

    public void updateFilter(final String tinh, final String huyen, long mingia, long maxgia, final long minS, final long maxS){

        Query query = null;
        if(tinh.equals("Tất cả")&&huyen.equals("Tất cả")){
            // truy van gia dien tich
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
                        adapterTabHost.notifyDataSetChanged();

                    }
                    else {
                        Log.e("CUONG", "Error getting documents: ", task.getException());

                    }
                }
            });

        }
        else {
            // truy van gia, dien tich, huyen
            if(tinh.equals("Tất cả")){
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
                            adapterTabHost.notifyDataSetChanged();

                        }
                        else {
                            Log.e("CUONG", "Error getting documents: ", task.getException());

                        }
                    }
                });
            }
            if(huyen.equals("Tất cả")){
                // truy van gia, dien tich, tinh
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
                            adapterTabHost.notifyDataSetChanged();

                        }
                        else {
                            Log.e("CUONG", "Error getting documents: ", task.getException());

                        }
                    }
                });

            }

            if(!huyen.equals("Tất cả")&&!tinh.equals("Tất cả")){
                // truy van gia, dien tich
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
                            adapterTabHost.notifyDataSetChanged();

                        }
                        else {
                            Log.e("CUONG", "Error getting documents: ", task.getException());

                        }
                    }
                });

            }

        }
    }

}

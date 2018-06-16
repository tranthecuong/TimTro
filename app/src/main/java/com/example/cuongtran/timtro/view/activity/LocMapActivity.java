package com.example.cuongtran.timtro.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.entity.FilterMap;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class LocMapActivity extends AppCompatActivity {
    TextView txtvGia, txtvDienTich;
    Button btnApply;
    Float a,b;
    Long a2,b2;
    Context mContext;
    CrystalRangeSeekbar Seekbargia,Seekbardientich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_map);
        mContext=this;
        Seekbargia=(CrystalRangeSeekbar)findViewById(R.id.seek_bar_gia_map2);
        Seekbardientich=(CrystalRangeSeekbar)findViewById(R.id.seek_bar_dientich_map2);
        txtvGia=(TextView)findViewById(R.id.giaMap2);
        txtvDienTich=(TextView)findViewById(R.id.dientich_map2);
        btnApply=(Button)findViewById(R.id.btn_apply_filter_map2) ;

        // set listener
        Seekbargia.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(Locale.US);
                DecimalFormat df = new DecimalFormat("#.0",symbols);
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
                Intent data = new Intent();
                FilterMap filterMap= new FilterMap(true,a,b,a2,b2);
                data.putExtra("filtermap",filterMap);
                setResult(300,data);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("CUONG","on back press");
        Intent data = new Intent();
        //FilterMap filterMap= new FilterMap(false,0.0f,15.0f,0,15);
        //data.putExtra("filtermap",filterMap);
        //setResult(200,data);
    }
}

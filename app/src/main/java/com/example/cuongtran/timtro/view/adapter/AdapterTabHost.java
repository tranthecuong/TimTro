package com.example.cuongtran.timtro.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.entity.TinDang;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterTabHost extends BaseAdapter {
    Context context;
    int layout;
    List<TinDang> arrTin ;

    public AdapterTabHost(Context context, int layout, List<TinDang> arrTin) {
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
        ImageView imageView = (ImageView)view.findViewById(R.id.img_itemTabHost);
        TextView textViewDiaChi =(TextView)view.findViewById(R.id.tvDiaChi_item_TabHost);
        TextView textViewGia =(TextView)view.findViewById(R.id.tvGia_item_TabHost);
        TextView textViewDienTich =(TextView)view.findViewById(R.id.tvDienTich_item_TabHost);

        String dc=arrTin.get(i).getChitietdiachi()+", "+arrTin.get(i).gettenquanhuyen()+", "+arrTin.get(i).gettenthanhpho();
        textViewDiaChi.setText(dc);
        textViewDiaChi.setEllipsize(TextUtils.TruncateAt.END);
        textViewDiaChi.setMaxLines(2);
        textViewGia.setText("Giá: "+String.valueOf(arrTin.get(i).getGia())+" đ");
        textViewDienTich.setText("Diện tích: "+String.valueOf(arrTin.get(i).getDientich())+" mét vuông");
        String url = arrTin.get(i).getAnh();
        Picasso.get().load(url)
               // .placeholder(R.drawable.ic_photo_camera_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(imageView);
        return view;
    }

}

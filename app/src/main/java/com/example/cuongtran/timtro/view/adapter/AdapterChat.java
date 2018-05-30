package com.example.cuongtran.timtro.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.entity.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterChat extends BaseAdapter {
    ArrayList<Chat> arr;
    Context context;
    int layout;
    TextView tvUser,tvChat,tvNgayDang ;

    public AdapterChat(ArrayList<Chat> arr, Context context, int layout) {
        this.arr = arr;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return arr.size();
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
        tvUser=(TextView)view.findViewById(R.id.username_chat);
        tvChat=(TextView)view.findViewById(R.id.text_chat);
        tvNgayDang=(TextView)view.findViewById(R.id.ngay_chat);
        tvUser.setText(arr.get(i).getUser());
        tvChat.setText(arr.get(i).getText());
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date=arr.get(i).getTimestamp().toDate();
        String ngay= sfd.format(date);
        tvNgayDang.setText(ngay);
        return view;
    }
}

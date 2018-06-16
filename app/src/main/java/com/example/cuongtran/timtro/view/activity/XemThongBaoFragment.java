package com.example.cuongtran.timtro.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.model.authenticate.FirebaseAuthenticator;
import com.example.cuongtran.timtro.presenter.PresenterXemThongBaoFragment;
import com.example.cuongtran.timtro.view.adapter.AdapterManHinh4;
import com.example.cuongtran.timtro.entity.ThongBao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class XemThongBaoFragment extends Fragment implements PresenterXemThongBaoFragment.IView {
    ListView lv;
    RelativeLayout relativeLayout;
    Button button;
    View rootView;
    List<ThongBao> arrayList;
    String idCurrentUser;
    AdapterManHinh4 adapterManHinh4;
    FirebaseAuthenticator firebaseAuthenticator= new FirebaseAuthenticator();
    PresenterXemThongBaoFragment presenterXemThongBaoFragment = new PresenterXemThongBaoFragment(this);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.manhinh4,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhxa();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String idTin = arrayList.get(i).getIdNhaTro();
                Intent intent = new Intent(getContext(), XemChiTietNhaTroActivity.class);
                intent.putExtra("CUONG", idTin);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if(firebaseAuthenticator.checkLogon()){
            idCurrentUser=firebaseAuthenticator.getUserId();
            // Neu co nguoi dang nhap roi thi lay cac thong bao ve cho vao listview
            relativeLayout.setVisibility(View.GONE);
            presenterXemThongBaoFragment.getData(idCurrentUser);
        }
        else {
            // Chưa co nguoi dang nhap, hien thi layout yeu cau dang nhap
            relativeLayout.setVisibility(View.VISIBLE);
        }

    }


    private void anhxa() {
        lv=(ListView)rootView.findViewById(R.id.lv_thongbao);
        relativeLayout=(RelativeLayout)rootView.findViewById(R.id.layout_tab_notification);
        button=(Button)rootView.findViewById(R.id.btn_tab_notification);
        arrayList = new ArrayList<>();
        adapterManHinh4= new AdapterManHinh4(getContext(),R.layout.item_tab4,arrayList);
        lv.setAdapter(adapterManHinh4);
    }

    @Override
    public void setData(List<ThongBao> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
        adapterManHinh4.notifyDataSetChanged();
    }
}

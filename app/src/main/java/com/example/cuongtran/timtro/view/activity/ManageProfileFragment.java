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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.model.authenticate.FirebaseAuthenticator;
import com.example.cuongtran.timtro.presenter.PresenterManageProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class ManageProfileFragment extends Fragment implements PresenterManageProfileFragment.IView {
    View rootView;
    TextView txtvTenTK ;
    ImageView imgAvatar;
    FirebaseAuthenticator firebaseAuthenticator= new FirebaseAuthenticator();
    PresenterManageProfileFragment presenterManageProfileFragment= new PresenterManageProfileFragment(this);
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("CUONG","onCreat 5");
        super.onCreate(savedInstanceState);
    }


    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.manhinh5,container,false);
        Log.e("CUONG","onCreatView 5");

        anhXa();
        txtvTenTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseAuthenticator.checkLogon()){
                    // da co nguoi dang nhap
                    Intent intent= new Intent(getActivity(),ProfileActivity.class);
                    startActivity(intent);

                }
                else
                {
                    // chua co nguoi dang nhạp, show dialog

                    Intent intent= new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);

                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        Log.e("CUONG","onResume 5");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("CUONG","onPause 5");
        super.onPause();
    }



    private void anhXa() {
        txtvTenTK=(TextView)rootView.findViewById(R.id.textViewTenTaiKhoan);
        imgAvatar=(ImageView)rootView.findViewById(R.id.imageView_avatar);
    }

    @Override
    public void onStart() {
        Log.e("CUONG","onStart 5");
        super.onStart();
        if(firebaseAuthenticator.checkLogon()){
            // neu da co nguoi dang nhap
            String id =firebaseAuthenticator.getUserId();
            presenterManageProfileFragment.getData(id);

        }else {
            // chua co nguoi dang nhap
            txtvTenTK.setText("Đăng Nhập");
            imgAvatar.setImageResource(R.drawable.default_avatar_160);
        }

    }

    @Override
    public void setData(String name, String link) {
        txtvTenTK.setText(name);
        if(!link.equals("nonono")){
            Picasso.get().load(link)
                    .into(imgAvatar);

        }
        else {
            imgAvatar.setImageResource(R.drawable.default_avatar_160);

        }
    }
}

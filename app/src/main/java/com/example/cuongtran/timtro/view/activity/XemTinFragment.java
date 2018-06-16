package com.example.cuongtran.timtro.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.view.adapter.TinDangAdapter;
import com.example.cuongtran.timtro.view.adapter.TinDangViewHolder;
import com.example.cuongtran.timtro.entity.CheckConnection;
import com.example.cuongtran.timtro.entity.Constant;
import com.example.cuongtran.timtro.entity.TinDang;
import com.example.cuongtran.timtro.view.adapter.EndlessScrollListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XemTinFragment extends Fragment {
    Context mContext;

    @BindView(R.id.rvNhaTro)
    public RecyclerView rvNhaTro;

    @BindView(R.id.fab)
    public FloatingActionButton fabAdd;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    ;
    private FirebaseFirestore db;

    private ArrayList<TinDang> tinDangList = new ArrayList<>();
    private TinDangAdapter tinDangAdapter;
    private boolean hasNextItem = true;
    private EndlessScrollListener onScroll;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getContext();
        return inflater.inflate(R.layout.manhinh1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        ButterKnife.bind(this, getView());
        tinDangAdapter = new TinDangAdapter(getContext(), tinDangList, new TinDangViewHolder.OnClickViewHolderListener() {
            @Override
            public void onClickItem(int position) {
                Intent intent = new Intent(getContext(), XemChiTietNhaTroActivity.class);
                intent.putExtra("CUONG", tinDangList.get(position).id);
                startActivity(intent);
            }

            @Override
            public void onClickAvater(int Position) {
                Intent intent = new Intent(getContext(), NguoiDangActivity.class);
                intent.putExtra("TAIKHOAN", tinDangList.get(Position).getIdtaikhoan());
                startActivity(intent);

            }



            @Override
            public void onClickButton(int Position) {
                if (CheckConnection.haveNetworkConnection(getContext())) {
                    if (mCurrentUser != null) {
                        // da co nguoi dang nhap
                        String idTin= tinDangList.get(Position).id;
                        String idTK = mCurrentUser.getUid();
                        luuTin(idTin,idTK);

                    } else {
                        // chua co nguoi dang nhạp, show dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert));
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
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        dialogInterface.cancel();

                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Chú ý");
                        alertDialog.show();
                    }
                } else {
                    Toast.makeText(getContext(), "Hãy kết nối internet", Toast.LENGTH_SHORT).show();
                }




            }


        });
        rvNhaTro.setAdapter(tinDangAdapter);
        // load more RV
        onScroll = new EndlessScrollListener((LinearLayoutManager) rvNhaTro.getLayoutManager()) {
            @Override
            public void onLoadMore() {
                loadData(false);
            }
        };
        rvNhaTro.addOnScrollListener(onScroll);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckConnection.haveNetworkConnection(getContext())) {
                    if (mCurrentUser != null) {
                        // da co nguoi dang nhap
                        Intent intent = new Intent(getContext(), DangTinActivity.class);
                        startActivity(intent);
                    } else {
                        // chua co nguoi dang nhạp, show dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert));
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
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        dialogInterface.cancel();

                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Chú ý");
                        alertDialog.show();
                    }
                } else {
                    Toast.makeText(getContext(), "Hãy kết nối internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void luuTin(final String idTin, final String idTK) {
        Toast.makeText(mContext, "Đã lưu", Toast.LENGTH_SHORT).show();
        // check da co tin tren fireStore chua
        db.collection(Constant.TIN_LUU).whereEqualTo(Constant.TIN_LUU_IDTIN,idTin)
                .whereEqualTo(Constant.TIN_LUU_IDTK,idTK)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().isEmpty()){
                    // them vao DB
                    Map<String,String> params = new HashMap<>();
                    params.put(Constant.TIN_LUU_IDTK,idTK);
                    params.put(Constant.TIN_LUU_IDTIN,idTin);
                    db.collection(Constant.TIN_LUU).add(params).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Log.e("CUONG","Luu Thanh Cong");
                        }
                    });

                }
                else {
                    Log.e("CUONG","Da luu roi");
                }
            }
        });

    }

    private void initData() {
        db = FirebaseFirestore.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        loadData(true);
    }

    private void loadData(final boolean isRefresh) {
        if (!isRefresh && !hasNextItem) {
            return;
        }

        CollectionReference collection = db.collection(Constant.NHA_TRO);
        Query query;

        if (isRefresh) {
            query = collection.orderBy("ngaydang", Query.Direction.DESCENDING).limit(Constant.LIMIT_ITEM);
        } else {
            tinDangAdapter.showLoadingItem(true);
            Timestamp pivotTime;
            if (tinDangList.isEmpty()) {
                pivotTime = new Timestamp(new Date());
            } else {
                pivotTime = tinDangList.get(tinDangList.size() - 1).getNgaydang();
            }
            query = collection.whereLessThanOrEqualTo("ngaydang", pivotTime).orderBy("ngaydang", Query.Direction.DESCENDING).limit(Constant.LIMIT_ITEM);
        }

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (isRefresh) {
                                tinDangList.clear();
                                TinDang tinDang = null;
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    tinDang = doc.toObject(TinDang.class).withId(doc.getId());
                                    tinDangList.add(tinDang);
                                }
                                tinDangAdapter.notifyDataSetChanged();
                                hasNextItem = tinDangList.size() == Constant.LIMIT_ITEM;
                            } else {
                                ArrayList<TinDang> moreTinDang = new ArrayList<>();
                                TinDang tinDang = null;
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    tinDang = doc.toObject(TinDang.class).withId(doc.getId());
                                    moreTinDang.add(tinDang);
                                }
                                moreTinDang.remove(0);
                                hasNextItem = moreTinDang.size() == Constant.LIMIT_ITEM;

                                int oldSize = tinDangList.size();
                                tinDangList.addAll(moreTinDang);
                                tinDangAdapter.notifyItemRangeInserted(oldSize, moreTinDang.size());
                                tinDangAdapter.showLoadingItem(false);
                            }
                        } else {
                            Log.e("CUONG", "Error getting documents: ", task.getException());
                            if (isRefresh) {

                            } else {
                                tinDangAdapter.showLoadingItem(false);
                            }
                        }
                        onScroll.setLoaded();
                    }
                });
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
    }
}

package com.example.cuongtran.timtro.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.entity.Constant;
import com.example.cuongtran.timtro.entity.TinDang;
import com.example.cuongtran.timtro.entity.WorkaroundMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SuaTin extends AppCompatActivity implements OnMapReadyCallback{
    EditText editTextChiTietDiaChi, editTextGia, editTextDienTich, editTextDienThoai, editTextMoTa;
    Button buttonSave;
    Spinner spinner1, spinner2;
    Context mContext;
    String[] arr1, arr2, arr3;

    ProgressDialog progressDialog;
    SupportMapFragment mapFragment;
    GoogleMap myMap;
    String kinhdo, vido;
    LatLng mLatLng;
    String diachi, dienthoai, mota,idQuanHuyen, idThanhPho;
    String dientich,gia;
    String idNhanDuoc;
    ScrollView scrollView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_tin);
        // an key board
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mContext = this;
        db=FirebaseFirestore.getInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idNhanDuoc = extras.getString("SUA").toString().trim();
        }
        anhXa();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(mContext, String.valueOf(i), Toast.LENGTH_SHORT).show();
                if (i == 0) {
                    ArrayAdapter<String> adp2 = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_spinner_item, arr2);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adp2);

                } else if (i == 1) {
                    ArrayAdapter<String> adp2 = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_spinner_item, arr3);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adp2);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lay DL tu cac edit text

                idQuanHuyen = spinner2.getSelectedItem().toString();
                idThanhPho = spinner1.getSelectedItem().toString();
                diachi = editTextChiTietDiaChi.getText().toString();
                gia = editTextGia.getText().toString();
                dientich = editTextDienTich.getText().toString();
                dienthoai = editTextDienThoai.getText().toString();
                mota = editTextMoTa.getText().toString();
                //Kiem tra DL
                if(diachi.equals("")||gia.equals("")||dienthoai.equals("")||dientich.equals("")||
                        mota.equals(""))
                    Toast.makeText(mContext, "Nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                else {
                    suaTin();
                }

            }
        });

    }

    private void anhXa() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_ChinhSua);
        mapFragment.getMapAsync(this);
        spinner1 = (Spinner) findViewById(R.id.spinner_Tinh_ChinhSua);
        spinner2 = (Spinner) findViewById(R.id.spinner_Quan_ChinhSua);
        arr1 = new String[]{"Hà Nội", "TP Hồ Chí Minh"};
        arr2 = new String[]{"Quận Ba Đình", "Quận Hoàn Kiếm", "Quận Hai Bà Trưng",
                "Quận Đống Đa", "Quận Tây Hồ", "Quận Cầu Giấy", "Quận Thanh Xuân", "Quận Hoàng Mai",
                "Quận Long Biên", "Quận Bắc Từ Liêm", "Huyện Thanh Trì", "Huyện Gia Lâm", "Huyện Đông Anh",
                "Huyện Sóc Sơn", "Quận Hà Đông", "Thị Xã Sơn Tây",
                "Huyện Ba Vì", "Huyện Phúc Thọ", "Huyện Thạch Thất", "Huyện Quốc Oai",
                "Huyện Chương Mỹ", "Huyện Đan Phượng", "Huyện Hoài Đức", "Huyện Thanh Oai", "Huyện Mỹ Đức",
                "Huyện Ứng Hòa", "Huyện Thường Tín", "Huyện Phú Xuyên", "Huyện Mê Linh", "Quận Nam Từ Liêm"};
        arr3 = new String[]{"Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6",
                "Quận 7", "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12", "Quận Gò Vấp", "Quận Tân Bình", "Quận Tân Phú", "Quận Bình Thạnh",
                "Quận Phú Nhuận", "Quận Thủ Đức", "Quận BÌnh Tân", "Huyện Bình Chánh", "Huyện Củ Chi",
                "Huyện Hóc Môn", "Huyện Nhà Bè", "Huyện Cần Giờ"};

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arr1);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adp1);

        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arr2);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adp2);


        editTextChiTietDiaChi = (EditText) findViewById(R.id.edit_ChiTietDiaChi_ChinhSua);
        editTextGia = (EditText) findViewById(R.id.edit_Gia_ChinhSua);
        editTextDienTich = (EditText) findViewById(R.id.edit_DienTich_ChinhSua);
        ;
        editTextDienThoai = (EditText) findViewById(R.id.edit_DienThoai_ChinhSua);
        ;
        editTextMoTa = (EditText) findViewById(R.id.edit_Mota_chinhSua);
        ;
        buttonSave = (Button) findViewById(R.id.btn_Edit_ChinhSua);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        myMap = googleMap;
        getDL();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myMap.setMyLocationEnabled(true);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                myMap.clear();
                mLatLng = new LatLng(latLng.latitude, latLng.longitude);
                myMap.addMarker(new MarkerOptions()
                        .position(mLatLng));
                //myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 17));
                //kinhdo=String.valueOf(mLatLng.latitude);
                //vido=String.valueOf(mLatLng.longitude);

            }
        });

        scrollView = (ScrollView) findViewById(R.id.scrollView_SuaTin);

        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_ChinhSua))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });

    }
    private  void getDL(){
        progressDialog= new ProgressDialog(mContext);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // FireBase Here
        db.collection("nhatro").document(idNhanDuoc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        TinDang ob =document.toObject(TinDang.class);
                        diachi = ob.getChitietdiachi();
                        gia = String.valueOf(ob.getGia());
                        dientich = String.valueOf(ob.getDientich());
                        dienthoai = ob.getDienthoai();
                        mota = ob.getMota();
                        editTextChiTietDiaChi.setText(diachi);
                        editTextGia.setText(gia);
                        editTextDienTich.setText(dientich);
                        editTextDienThoai.setText(dienthoai);
                        editTextMoTa.setText(mota);
                        kinhdo = ob.getKinhdo();
                        vido = ob.getVido();

                        mLatLng = new LatLng(Double.parseDouble(vido), Double.parseDouble(kinhdo));
                        myMap.addMarker(new MarkerOptions().position(mLatLng)
                                .title(diachi)
                        );
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 17));
                        progressDialog.dismiss();




                    } else {
                        Toast.makeText(mContext, "Tin đã bị xóa", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else {
                    Log.d("CUONG", "get failed with ", task.getException());
                }





            }
        });



    }
    private void suaTin(){
        buttonSave.setEnabled(false);
        buttonSave.setVisibility(View.GONE);
        progressDialog= new ProgressDialog(mContext);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tenthanhpho", idThanhPho);
        params.put("tenquanhuyen", idQuanHuyen);
        params.put("chitietdiachi", diachi);
        params.put("gia", Long.parseLong(gia));
        params.put("dientich", Long.parseLong(dientich));
        params.put("dienthoai", dienthoai);
        params.put("mota", mota);
        params.put("ngaydang", FieldValue.serverTimestamp());
        String kinhdo=String.valueOf(mLatLng.longitude);
        String vido = String.valueOf(mLatLng.latitude);
        params.put("kinhdo",kinhdo);
        params.put("vido",vido);

        db.collection(Constant.NHA_TRO).document(idNhanDuoc)
                .set(params, SetOptions.merge())
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(mContext, "Sửa thành công ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                }
            }
        });

    }
}

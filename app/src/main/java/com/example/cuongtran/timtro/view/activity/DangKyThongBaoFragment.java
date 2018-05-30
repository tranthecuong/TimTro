package com.example.cuongtran.timtro.view.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.presenter.PresenterDangKiThongBao;
import com.example.cuongtran.timtro.model.authenticate.FirebaseAuthenticator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import static com.example.cuongtran.timtro.view.activity.XemTinMapFragment.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class DangKyThongBaoFragment extends Fragment implements OnMapReadyCallback,PresenterDangKiThongBao.Iview {
    boolean mLocationPermissionGranted = false;
    View rootView;
    GoogleMap mMap;
    Spinner spinnerBanKinh, spinnerGia,spinnerDienTich;
    Button button,buttonHuy;
    RelativeLayout layout_huythongbao;
    String[] dataSpinnerBanKinh, dataSpinnerGia,dataSpinnerDientich;
    SupportMapFragment fragment;
    Context mContext;
    Marker center;
    Circle myCircle;
    LatLng mylatLng;
    int radiusInMeters = 1000;
    //red outline
    int strokeColor = 0xffff0000;
    //opaque red fill
    //int shadeColor = Color.parseColor("#ffffff");
    PresenterDangKiThongBao presenterDangKiThongBao= new PresenterDangKiThongBao(this);
    FirebaseAuthenticator firebaseAuthenticator= new FirebaseAuthenticator();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.manhinh3, container, false);
        mContext = getContext();
        anhxa();
        fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_tab3);
        fragment.getMapAsync(this);
        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_huythongbao.setVisibility(View.GONE);
                presenterDangKiThongBao.turnOffNotification();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firebaseAuthenticator.checkLogon()) {
                    Toast.makeText(mContext, "Bạn Cần Đăng nhập trước", Toast.LENGTH_SHORT).show();
                } else {
                        // cho phep nguoi dung dang ki nhan tin
                        if (mylatLng == null) {
                            Toast.makeText(mContext, "Bạn cần chọn địa điểm trên bản đồ trước !", Toast.LENGTH_SHORT).show();
                        } else {
                            //thoa man
                            layout_huythongbao.setVisibility(View.VISIBLE);
                            //day du lieu len tieu chi tren firebase
                            ArrayList<String> arr = new ArrayList<>();
                            arr.add(String.valueOf(mylatLng.longitude));
                            arr.add(String.valueOf(mylatLng.latitude));
                            arr.add(String.valueOf(getBanKinh()));
                            arr.add(String.valueOf(getGia()));
                            arr.add(String.valueOf(getDienTich()));
                            presenterDangKiThongBao.turnOnNotification(arr);

                        }





                }


            }
        });

        spinnerBanKinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(mylatLng!=null){
                    if (myCircle != null) myCircle.remove();
                    double bankinh = getBanKinh();
                    CircleOptions circleOptions = new CircleOptions().center(mylatLng).radius(radiusInMeters * bankinh).strokeColor(strokeColor).strokeWidth(2);
                    myCircle = mMap.addCircle(circleOptions);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        if (firebaseAuthenticator.checkLogon()) {
            presenterDangKiThongBao.checkTrangThai();
        } else {
            //chua co nguoi dang nhap
            layout_huythongbao.setVisibility(View.GONE);

        }

        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void anhxa() {
        spinnerBanKinh = (Spinner) rootView.findViewById(R.id.spinner_bankinh_tab3);
        spinnerGia = (Spinner) rootView.findViewById(R.id.spinner_gia_tab3);
        spinnerDienTich=(Spinner)rootView.findViewById(R.id.spinner_dientich_tab3) ;
        button = (Button) rootView.findViewById(R.id.btn_tab3);
        buttonHuy=(Button)rootView.findViewById(R.id.btn_Huy_Thong_Bao);
        layout_huythongbao=(RelativeLayout)rootView.findViewById(R.id.success);
        dataSpinnerBanKinh = new String[]{"0.5 Km", "1 Km", "2 Km", "3 Km", "5 Km", "10 Km", "15 Km", "20 Km", "25 Km"};
        dataSpinnerGia = new String[]{"Dưới 1 triệu", "Dưới 1.5 triệu", "Dưới 2 triệu", "Dưới 2.5 triệu", "Dưới 3 triệu",
                "Dưới 4 triệu", "Dưới 5 triệu", "Dưới 7 triệu", "Dưới 10 triệu", "Dưới 15 triệu"};
        dataSpinnerDientich= new String[]{"Trên 5 mét vuông","Trên 10 mét vuông","Trên 15 mét vuông","Trên 20 mét vuông",
                "Trên 25 mét vuônh","Trên 30 mét vuông","Trên 40 mét vuông","Trên 50 mét vuông","Trên 60 mét vuông"};
        ArrayAdapter<String> adp3 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, dataSpinnerDientich);
        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDienTich.setAdapter(adp3);

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, dataSpinnerBanKinh);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBanKinh.setAdapter(adp1);
        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, dataSpinnerGia);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGia.setAdapter(adp2);

    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            updateLocationUI();
        } else {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "Đã cho phép", Toast.LENGTH_SHORT).show();
                    mLocationPermissionGranted = true;
                } else {
                    Toast.makeText(mContext, "Bạn cần cấp phép GPS để sử dụng ứng dụng !", Toast.LENGTH_SHORT).show();
                }
            }
        }
        updateLocationUI();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                presenterDangKiThongBao.checkInternet(getContext());

            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (myCircle != null) myCircle.remove();
                if (center != null) center.remove();
                mylatLng = new LatLng(latLng.latitude, latLng.longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(mylatLng);
                int height = 100;
                int width = 100;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                center = mMap.addMarker(markerOptions);
                double bankinh = getBanKinh();
                CircleOptions circleOptions = new CircleOptions().center(mylatLng).radius(radiusInMeters * bankinh).strokeColor(strokeColor).strokeWidth(2);
                myCircle = mMap.addCircle(circleOptions);


            }
        });


    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private long getDienTich(){
        long dt=5;
        int vitri= spinnerBanKinh.getSelectedItemPosition();
        switch(vitri){
            case 0: dt=5;break;
            case 1: dt=10;break;
            case 2: dt=15;break;
            case 3: dt=20;break;
            case 4: dt=25;break;
            case 5: dt=30;break;
            case 6: dt=40;break;
            case 7: dt=50;break;
            case 8: dt=60;break;
        }

        return  dt;
    };



    private double getGia() {
        double gia = 1;
        int vitri = spinnerGia.getSelectedItemPosition();
        switch (vitri) {
            case 0:
                gia = 1;
                break;
            case 1:
                gia = 1.5;
                break;
            case 2:
                gia = 2;
                break;
            case 3:
                gia = 2.5;
                break;
            case 4:
                gia = 3;
                break;
            case 5:
                gia = 4;
                break;
            case 6:
                gia = 5;
                break;
            case 7:
                gia = 7;
                break;
            case 8:
                gia = 10;
                break;
            case 9:
                gia = 15;
                break;

        }

        return gia;

    }

    private double getBanKinh() {
        double bankinh = 0.5;
        int vitri = spinnerBanKinh.getSelectedItemPosition();
        switch (vitri) {
            case 0:
                bankinh = 0.5
                ;
                break;
            case 1:
                bankinh = 1
                ;
                break;
            case 2:
                bankinh = 2
                ;
                break;
            case 3:
                bankinh = 3
                ;
                break;
            case 4:
                bankinh = 5
                ;
                break;
            case 5:
                bankinh = 10
                ;
                break;
            case 6:
                bankinh = 15
                ;
                break;
            case 7:
                bankinh = 20
                ;
                break;
            case 8:
                bankinh = 25
                ;
                break;
        }

        return bankinh;
    }


    @Override
    public void showMess(String mess) {
        Toast.makeText(mContext, mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setVisibility(int i) {
        layout_huythongbao.setVisibility(i);
    }
}

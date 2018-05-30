package com.example.cuongtran.timtro.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.presenter.PresenterChiTietBanDo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class ChiTietBanDoActivity extends AppCompatActivity implements OnMapReadyCallback,PresenterChiTietBanDo.IChiTiet {
    ProgressDialog progressDialog;
    String idNhanDuoc;
    GoogleMap myMap;
    PresenterChiTietBanDo presenterChiTietBanDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_ban_do);
        presenterChiTietBanDo= new PresenterChiTietBanDo(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_ChiTietBanDo);
        mapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idNhanDuoc = extras.getString("ChiTiet");
            Log.e("CUONG","map nhan duoc "+idNhanDuoc);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
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

        // FireBase Here
        //getData();
        presenterChiTietBanDo.getData(idNhanDuoc);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");
        progressDialog.show();

    }

    @Override
    public void hideDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void success(String diaChi, String kinhdo, String vido) {

        LatLng bk = new LatLng(Double.parseDouble(vido),Double.parseDouble(kinhdo));
        Marker marker;
        marker=myMap.addMarker(new MarkerOptions().position(bk)
                .title(diaChi)
        );
        marker.showInfoWindow();

        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bk,14));
    }
}

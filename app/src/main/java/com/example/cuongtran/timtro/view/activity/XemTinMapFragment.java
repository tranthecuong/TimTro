package com.example.cuongtran.timtro.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuongtran.timtro.presenter.PresenterXemTinMap;
import com.example.cuongtran.timtro.view.activity.LocMapActivity;
import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.view.activity.XemChiTietNhaTroActivity;
import com.example.cuongtran.timtro.entity.FilterMap;
import com.google.android.gms.location.LocationListener;
import com.example.cuongtran.timtro.entity.TinDang;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class XemTinMapFragment extends android.support.v4.app.Fragment
        implements OnMapReadyCallback
        , GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        PresenterXemTinMap.IView{
    PresenterXemTinMap presenterXemTinMap= new PresenterXemTinMap(this);
    boolean mLocationPermissionGranted = false;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;
    View rootView;
    GoogleMap mMap;
    Spinner spinner;
    Button button;
    Button btn_filter;
    TextView tieuchi;
    String[] dataSpinner;
    SupportMapFragment fragment;
    Context mContext;
    float a=0.0f;
    float b=15.0f;
    long a2=0,b2=100;

    ArrayList<TinDang> arrTinDang = new ArrayList<>();
    String ngayDang = "";
    static final int CODE_REQUEST_LOCATION = 1111;
    int allow;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Marker center = null;
    Circle myCircle;
    LatLng mylatLng;
    int radiusInMeters = 1000;
    //red outline
    int strokeColor = 0xffff0000;
    //opaque red fill
    int shadeColor = Color.parseColor("#ffffff");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        Log.e("CUONG","onCreatM2");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("CUONG","onStartM2");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("CUONG","onResumeM2");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("CUONG","onPauseM2");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("CUONG","onStopM2");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("CUONG","onDestroyViewM2");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("CUONG","onCreatViewM2");
        rootView = inflater.inflate(R.layout.manhinh2, container, false);
        allow = 0;
        anhxa();
        fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_tab2);
        fragment.getMapAsync(this);
        return rootView;

    }


    private void drawCircle(int i) {
        if (i == 0) {
            if (myCircle != null) {
                myCircle.remove();
            }
            if (center != null) {
                center.remove();
            }
            mMap.clear();
            setMarkerToMap();


        } else {
            if (myCircle != null) {
                myCircle.remove();
            }
            if (center != null) {
                center.remove();
            }
            mMap.clear();
            // tinh toan khoang cach tu mang
            int length = arrTinDang.size();
            //show all maker
            for (int j = 0; j < length; j++) {
                Double kinhdo = Double.parseDouble(arrTinDang.get(j).getKinhdo());
                Double vido = Double.parseDouble(arrTinDang.get(j).getVido());
                long gia =arrTinDang.get(j).getGia();
                long dienTich=arrTinDang.get(j).getDientich();
                float[] results = new float[1];
                Location.distanceBetween(mylatLng.latitude, mylatLng.longitude,
                        vido, kinhdo,
                        results);
                //Log.e("CUONG","khoang Cach= "+String.valueOf(results[0]));
                if ((results[0] <= i * 1000)&&(gia>=a*1000000)&&(gia<=b*1000000)&&(dienTich>=a2)&&(dienTich<=b2)) {

                    SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date date = arrTinDang.get(j).getNgaydang().toDate();
                    String ngayDang = sfd.format(date);

                    String idTin = arrTinDang.get(j).id;
                    HashMap hashMap = new HashMap();
                    hashMap.put("id", idTin);
                    LatLng temp = new LatLng(vido, kinhdo);
                    String dc = arrTinDang.get(j).getChitietdiachi() + ", " + arrTinDang.get(j).gettenquanhuyen() + ", " + arrTinDang.get(j).gettenthanhpho();
                    Marker marker;
                    marker = mMap.addMarker(new MarkerOptions().position(temp)
                            .title(dc)
                            .snippet("Giá: " + gia + "\n" + "Diện Tích: " + dienTich + " mét vuông" + "\n" +
                                    "Ngày đăng: " + ngayDang)

                    );
                    marker.setTag(hashMap);

                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            LinearLayout info = new LinearLayout(mContext);
                            info.setOrientation(LinearLayout.VERTICAL);

                            TextView title = new TextView(mContext);
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.CENTER);
                            title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            TextView snippet = new TextView(mContext);
                            snippet.setTextColor(Color.GRAY);
                            snippet.setText(marker.getSnippet());

                            info.addView(title);
                            info.addView(snippet);

                            return info;
                        }
                    });


                }


            }


            CircleOptions circleOptions = new CircleOptions().center(mylatLng).radius(radiusInMeters * i).strokeColor(strokeColor).strokeWidth(2);
            myCircle = mMap.addCircle(circleOptions);

            MarkerOptions markerOptions = new MarkerOptions().position(mylatLng);
            int height = 100;
            int width = 100;
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            center = mMap.addMarker(markerOptions);
            //center.hideInfoWindow();
        }

    }



    public void getduLieu() {
     arrTinDang.clear();
      if (allow == 1) mMap.clear();
     presenterXemTinMap.getData();

    }

    private void setMarkerToMap() {
        int length = arrTinDang.size();
        //show all maker
        for (int j = 0; j < length; j++) {
            float gia = arrTinDang.get(j).getGia();
            long dienTich = arrTinDang.get(j).getDientich();
            if ((gia>=a*1000000)&&(gia<=b*1000000)&&(dienTich>=a2)&&(dienTich<=b2))
            { SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = arrTinDang.get(j).getNgaydang().toDate();
            ngayDang = sfd.format(date);
            Double kinhdo = Double.parseDouble(arrTinDang.get(j).getKinhdo());
            Double vido = Double.parseDouble(arrTinDang.get(j).getVido());
            String idTin = arrTinDang.get(j).id;
            HashMap hashMap = new HashMap();
            hashMap.put("id", idTin);

            LatLng temp = new LatLng(vido, kinhdo);
            String dc = arrTinDang.get(j).getChitietdiachi() + ", " + arrTinDang.get(j).gettenquanhuyen() + ", " + arrTinDang.get(j).gettenthanhpho();
            Marker marker;
            marker = mMap.addMarker(new MarkerOptions().position(temp)
                    .title(dc)
                    .snippet("Giá: " + gia + "\n" + "Diện Tích: " + dienTich + " mét vuông" + "\n" +
                            "Ngày đăng: " + ngayDang)
            );
            marker.setTag(hashMap);

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    LinearLayout info = new LinearLayout(mContext);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(mContext);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(mContext);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
        }


        }
        if (mylatLng != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mylatLng, 13);
            mMap.animateCamera(cameraUpdate);
        }


    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    private void anhxa() {
        spinner = (Spinner) rootView.findViewById(R.id.spinner_tab2);
        button = (Button) rootView.findViewById(R.id.btn_timkiem_tab2);
        tieuchi=(TextView)rootView.findViewById(R.id.tieuchimap2) ;
        btn_filter = (Button) rootView.findViewById(R.id.btn_filter_map2);
        dataSpinner = new String[]{"Tất cả", "1 Kilomet", "2 Kilomet", "3 Kilomet", "5 Kilomet", "10 Kilomet", "15 Kilomet"};
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, dataSpinner);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp1);
    }

    private void getLocationPermission() {
        Log.e("CUONG", "getLocationPermission map2");

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
        Log.e("CUONG", "onMapready Map2");
        mMap = googleMap;
        presenterXemTinMap.checkInternet(getContext());
//        if (CheckConnection.haveNetworkConnection(getContext())) {
//            //neu co mang;
//            getLocationPermission();
//
//        } else {
//            Toast.makeText(mContext, "Kiểm tra lại internet", Toast.LENGTH_SHORT).show();
//        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                allow = 1;
                //getduLieu();
                Log.e("CUONG", "onMapLoaded Map2");
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Tat inforwindow cua center khi bam vao
                if(marker.equals(center)){
                    return true;
                }
                return false;
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
                //center.hideInfoWindow();


            }
        });
        //set su kien khi nhan vao info window
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                HashMap data = (HashMap) marker.getTag();
                String id = data.get("id").toString();
                Log.e("CUONG",id);
                //chuyen sang chiTiet activity
                Intent intent = new Intent(getContext(), XemChiTietNhaTroActivity.class);
                intent.putExtra("CUONG", id);
                getContext().startActivity(intent);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //{"Tất cả","1 Kilomet", "2 Kilomet","3 Kilomet","5 Kilomet", "10 Kilomet", "15 Kilomet"};
                int r = spinner.getSelectedItemPosition();
                switch (r) {
                    case 0:
                        drawCircle(0);
                        break;
                    case 1:
                        drawCircle(1);
                        break;
                    case 2:
                        drawCircle(2);
                        break;
                    case 3:
                        drawCircle(3);
                        break;
                    case 4:
                        drawCircle(5);
                        break;
                    case 5:
                        drawCircle(10);
                        break;
                    case 6:
                        drawCircle(15);
                        break;

                }

                //Log.e("CUONG",String.valueOf(mylatLng.latitude)+" "+String.valueOf(mylatLng.longitude));


            }
        });
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LocMapActivity.class);
                startActivityForResult(intent, 300);

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==300){
            FilterMap packet = data.getParcelableExtra("filtermap");
            boolean action =packet.isFreshNeed();
            if(action==true){
                a=packet.getMinGia();
                b=packet.getMaxGia();
                a2=packet.getMinDienTich();
                b2=packet.getMaxDienTich();
                String gia;
                if(a==0.0&&b==15.0){
                    gia="";
                }else {
                    gia=a+" tr - "+b+" tr ";
                }
                String dt;
                if(a2==0&&b2==100){
                    dt="";
                }else {
                    dt=a2+" m2 - "+b2+" m2";
                }
                if(gia.equals("")&&dt.equals("")){
                    tieuchi.setText("tất cả");
                }else{
                    tieuchi.setText(gia+" : "+dt);
                }
            }



        }
        else {
            //if(resultCode==200)Log.e("CUONG","200");
        }
    }

    private void updateLocationUI() {

        Log.e("CUONG", "updateUI map2");
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("CUONG", "onConnected_map 2");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //Log.e("CUONG","onLocation changed map 2 ");

        //mLastLocation = location;
        if (center != null) {
            //Log.e("CUONG","location changed but not excuted ");
            return;
        }

        //Place current location marker

        mylatLng = new LatLng(location.getLatitude(), location.getLongitude());
        //Log.e("CUONG",String.valueOf(mylatLng.latitude)+" "+String.valueOf(mylatLng.longitude));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mylatLng);
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        center = mMap.addMarker(markerOptions);
        //center.hideInfoWindow();
        getduLieu();


    }


    @Override
    public void setData(ArrayList<TinDang> arr) {
        // lay duoc du lieu ve
        arrTinDang.addAll(arr);
        setMarkerToMap();

    }

    @Override
    public void haveInternet(boolean status) {
        if(status==true){
            getLocationPermission();
        }else {
            Toast.makeText(mContext, "Kiểm tra lại internet", Toast.LENGTH_SHORT).show();
        }
    }
}

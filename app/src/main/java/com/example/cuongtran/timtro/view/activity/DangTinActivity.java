package com.example.cuongtran.timtro.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.entity.WorkaroundMapFragment;
import com.example.cuongtran.timtro.presenter.PresenterDangTin;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.cuongtran.timtro.view.activity.ProfileActivity.CODE_CAMERA_REQUEST;
import static com.example.cuongtran.timtro.view.activity.ProfileActivity.CODE_GALLERY_REQUEST;
import static com.example.cuongtran.timtro.view.activity.ProfileActivity.MY_PERMISSIONS_REQUEST_CAMERA;
import static com.example.cuongtran.timtro.view.activity.ProfileActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class DangTinActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,PresenterDangTin.IView{
    PresenterDangTin presenterDangTin= new PresenterDangTin(this);

    Uri imgAvatarUri =null;
    ProgressDialog progressDialog;

    Spinner spinnerQuan, spinnerTinh;
    ImageView imageViewNhaTro;
    EditText editTextChiTietDiaChi, editTextGia, editTextDienTich, editTextDienThoai, editTextMoTa;
    Button buttonDangTin;
    String[] dataSpinner1, dataSpinner2, dataSpinner22;
    Context mContext = this;
    Bitmap resizedBitmap = null;
    String idQuanHuyen, idThanhPho, chiTietDiaChi, dienThoai, moTa;
    String gia,dienTich;
    GoogleMap mMap;
    SupportMapFragment fragment;
    LatLng mLatLng=null;
    ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin);
        // an key board
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        anhxa();
        //set su kien cho nut lay anh
        imageViewNhaTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] s = {"Chụp ảnh mới", "Chọn từ album"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Bạn muốn lấy ảnh từ đâu ?");
                builder.setItems(s, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            // Chup anh moi
                            chooseFromCamera();

                        } else {
                            // Lay anh tu album
                            chooseFromStorage();


                        }

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        //su kien nut dang tin
        buttonDangTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiem tra data
                idQuanHuyen = spinnerQuan.getSelectedItem().toString();
                idThanhPho = spinnerTinh.getSelectedItem().toString();
                chiTietDiaChi = editTextChiTietDiaChi.getText().toString();
                gia = editTextGia.getText().toString();
                dienTich = editTextDienTich.getText().toString();
                dienThoai = editTextDienThoai.getText().toString();
                moTa = editTextMoTa.getText().toString();
                presenterDangTin.dangTin(mContext,idQuanHuyen,idThanhPho,chiTietDiaChi,gia,dienTich,dienThoai,moTa,resizedBitmap,mLatLng);
            }

        });
        spinnerTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(mContext, String.valueOf(i), Toast.LENGTH_SHORT).show();
                if (i == 0) {
                    ArrayAdapter<String> adp2 = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_spinner_item, dataSpinner2);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerQuan.setAdapter(adp2);

                } else if (i == 1) {
                    ArrayAdapter<String> adp2 = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_spinner_item, dataSpinner22);
                    adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerQuan.setAdapter(adp2);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void chooseFromStorage() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // kiem tra permission
            if (ContextCompat.checkSelfPermission(DangTinActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                //yeu cau cap quyen

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(DangTinActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(DangTinActivity.this)
                            .setTitle("Yêu cầu cấp quyền")
                            .setMessage("Bạn cần cấp quyền để sử dụng được tính năng")
                            .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();

                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    ActivityCompat.requestPermissions(DangTinActivity.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


                                }
                            })
                            .create()
                            .show();

                } else {

                    // No explanation needed; request the permission

                    ActivityCompat.requestPermissions(DangTinActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }

            }
            else {
                //permission da cho phep
                setImageFromStorage();


            }


        }
        else {
            setImageFromStorage();


        }

    }



    private void chooseFromCamera() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // kiem tra permission
            if (ContextCompat.checkSelfPermission(DangTinActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                //yeu cau cap quyen

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(DangTinActivity.this,
                        Manifest.permission.CAMERA)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(DangTinActivity.this)
                            .setTitle("Yêu cầu cấp quyền")
                            .setMessage("Bạn cần cấp quyền để sử dụng được tính năng")
                            .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();

                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    ActivityCompat.requestPermissions(DangTinActivity.this,
                                            new String[]{Manifest.permission.CAMERA},
                                            MY_PERMISSIONS_REQUEST_CAMERA);


                                }
                            })
                            .create()
                            .show();

                } else {

                    // No explanation needed; request the permission

                    ActivityCompat.requestPermissions(DangTinActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }

            }
            else {
                //permission da cho phep
                setImageFromCamera();


            }


        }
        else {
            setImageFromCamera();

        }





    }
    private void setImageFromStorage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        this.startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh"),CODE_GALLERY_REQUEST);
    }

    private void setImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent,CODE_CAMERA_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == CODE_GALLERY_REQUEST) && (resultCode == RESULT_OK) && data != null) {
            //Toast.makeText(mContext, "haaaaa", Toast.LENGTH_SHORT).show();
            Bitmap bitmap =null;
            imgAvatarUri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(imgAvatarUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                int a = bitmap.getWidth();
                int b = bitmap.getHeight();
                int b2 = b * 1080 / a;
                resizedBitmap = Bitmap.createScaledBitmap(
                        bitmap, 1080, b2, false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            imageViewNhaTro.setImageBitmap(resizedBitmap);

        }

        if ((requestCode == CODE_CAMERA_REQUEST) && (resultCode == RESULT_OK) && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            int a = bitmap.getWidth();
            int b = bitmap.getHeight();
            int b2 = b * 1080 / a;
            resizedBitmap = Bitmap.createScaledBitmap(
                    bitmap, 1080, b2, false);
            imageViewNhaTro.setImageBitmap(resizedBitmap);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    setImageFromStorage();

                } else {
                    Toast.makeText(mContext, "Bạn không thể sử dụng tính năng này khi không cấp quyền", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case  2 :{

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    setImageFromCamera();

                } else {
                    Toast.makeText(mContext, "Bạn không thể sử dụng tính năng này khi không cấp quyền", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;



            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    private void anhxa() {
        fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_DangTin);
        fragment.getMapAsync(this);
        //gan data cho spinner
        spinnerTinh = (Spinner) findViewById(R.id.spinner_Tinh_DangTin);
        spinnerQuan = (Spinner) findViewById(R.id.spinner_Quan_DangTin);

        dataSpinner1 = new String[]{"Hà Nội", "TP Hồ Chí Minh"};
        dataSpinner2 = new String[]{"Quận Ba Đình", "Quận Hoàn Kiếm", "Quận Hai Bà Trưng",
                "Quận Đống Đa", "Quận Tây Hồ", "Quận Cầu Giấy", "Quận Thanh Xuân", "Quận Hoàng Mai",
                "Quận Long Biên", "Quận Bắc Từ Liêm", "Huyện Thanh Trì", "Huyện Gia Lâm", "Huyện Đông Anh",
                "Huyện Sóc Sơn", "Quận Hà Đông", "Thị Xã Sơn Tây",
                "Huyện Ba Vì", "Huyện Phúc Thọ", "Huyện Thạch Thất", "Huyện Quốc Oai",
                "Huyện Chương Mỹ", "Huyện Đan Phượng", "Huyện Hoài Đức", "Huyện Thanh Oai", "Huyện Mỹ Đức",
                "Huyện Ứng Hòa", "Huyện Thường Tín", "Huyện Phú Xuyên", "Huyện Mê Linh", "Quận Nam Từ Liêm"};
        dataSpinner22 = new String[]{"Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6",
                "Quận 7", "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12", "Quận Gò Vấp", "Quận Tân Bình", "Quận Tân Phú", "Quận Bình Thạnh",
                "Quận Phú Nhuận", "Quận Thủ Đức", "Quận BÌnh Tân", "Huyện Bình Chánh", "Huyện Củ Chi",
                "Huyện Hóc Môn", "Huyện Nhà Bè", "Huyện Cần Giờ"};

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataSpinner1);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTinh.setAdapter(adp1);

        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, dataSpinner2);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuan.setAdapter(adp2);

        //anh xa
        imageViewNhaTro = (ImageView) findViewById(R.id.imgAnh_DangTin);
        editTextChiTietDiaChi = (EditText) findViewById(R.id.edit_ChiTietDiaChi_DangTin);
        editTextGia = (EditText) findViewById(R.id.edit_Gia_DangTin);
        editTextDienTich = (EditText) findViewById(R.id.edit_DienTich_DangTin);
        ;
        editTextDienThoai = (EditText) findViewById(R.id.edit_DienThoai_DangTin);
        ;
        editTextMoTa = (EditText) findViewById(R.id.edit_Mota_DangTin);
        ;
        buttonDangTin = (Button) findViewById(R.id.btn_DangTin_DangTin);


    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mLatLng = new LatLng(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions()
                        .position(mLatLng));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 17));


            }
        });

        // dissable scroll event

        mScrollView = (ScrollView) findViewById(R.id.scrollView_DangTin);

        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_DangTin))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });

    }

    @Override
    public void showMess(String mess) {
        Toast.makeText(mContext, mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(Boolean vis) {
        if(vis==true){
            buttonDangTin.setEnabled(false);
            buttonDangTin.setVisibility(View.GONE);
            progressDialog= new ProgressDialog(mContext);
            progressDialog.setMessage("Đang đăng tin......");
            progressDialog.setTitle("Đăng tin");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }else {
            progressDialog.dismiss();
        }

    }

    @Override
    public void done() {
        finish();
    }
}

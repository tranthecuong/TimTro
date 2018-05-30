package com.example.cuongtran.timtro.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuongtran.timtro.presenter.PresenterXemChiTietNhaTro;
import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.view.adapter.AdapterChat;
import com.example.cuongtran.timtro.model.authenticate.FirebaseAuthenticator;
import com.example.cuongtran.timtro.entity.Chat;
import com.example.cuongtran.timtro.entity.CheckConnection;
import com.example.cuongtran.timtro.entity.TinDang;
import com.example.cuongtran.timtro.entity.WorkaroundMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class XemChiTietNhaTroActivity extends AppCompatActivity implements OnMapReadyCallback,PresenterXemChiTietNhaTro.IchiTietNhaTro {

    String idNhanDuoc ;
    ImageView img,imgAvatar;
    TextView txtvNguoiDang,txtvNgayDang,txtvDiaChi,txtvGia,txtvDienTich,txtvSdt,txtvMoTa;
    Button btnLuu,btnChat;
    SupportMapFragment mapFragment;
    ProgressDialog progressDialog ;
    GoogleMap myMap;
    String kinhdo,vido;
    String diaChi;
    String idNguoiDang,tentk;
    long gia,dientich;
    Context context;
    ScrollView scrollView;
    ArrayList<Chat> arrChat = new ArrayList<>();
    ListView lvChat;
    EditText editTextChat;
    AdapterChat adapterChat;
    PresenterXemChiTietNhaTro presenterXemChiTietNhaTro= new PresenterXemChiTietNhaTro(this);
    FirebaseAuthenticator firebaseAuthenticator = new FirebaseAuthenticator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_nha_tro);
        context=getApplicationContext();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idNhanDuoc = extras.getString("CUONG").toString().trim();
        }
        //Log.e("CUONG",idNhanDuoc);
        anhXa();
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NguoiDangActivity.class);
                intent.putExtra("TAIKHOAN",idNguoiDang);
                startActivity(intent);
            }
        });

        // Su kien nut luu tin
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CheckConnection.haveNetworkConnection(context)){
                    if(firebaseAuthenticator.checkLogon()){
                        // da co nguoi dang nhap
                        String idTK = firebaseAuthenticator.getUserId();
                        presenterXemChiTietNhaTro.luuTin(idNhanDuoc,idTK);
                        Toast.makeText(context, "Đã lưu", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        // chua co nguoi dang nhạp, show dialog

                        AlertDialog.Builder builder =new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.Theme_AppCompat_Light_Dialog_Alert));
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
                                        Intent intent= new Intent(context,LoginActivity.class);
                                        startActivity(intent);
                                        dialogInterface.cancel();

                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Chú ý");
                        alertDialog.show();



                    }


                }
                else Toast.makeText(context, "Hãy kết nối internet", Toast.LENGTH_SHORT).show();



            }
        });

        //su kien nut chat
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CheckConnection.haveNetworkConnection(context)){

                    if(firebaseAuthenticator.checkLogon()){
                        // da co nguoi dang nhap
                        addChatToList();

                    }
                    else
                    {
                        // chua co nguoi dang nhạp, show dialog

                        AlertDialog.Builder builder =new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.Theme_AppCompat_Light_Dialog_Alert));
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
                                        Intent intent= new Intent(context,LoginActivity.class);
                                        startActivity(intent);
                                        dialogInterface.cancel();

                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Chú ý");
                        alertDialog.show();

                    }


                }
                else Toast.makeText(context, "Hãy kết nối internet", Toast.LENGTH_SHORT).show();



            }
        });



    }

    private void addChatToList() {
        String text =editTextChat.getText().toString().trim();
        presenterXemChiTietNhaTro.addChat(text,tentk,idNhanDuoc);

    }

    private void anhXa() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_ChiTiet);
        mapFragment.getMapAsync(this);
        img=(ImageView)findViewById(R.id.img_AnhNhaTro_ChiTiet);
        imgAvatar=(ImageView)findViewById(R.id.imageViewNguoiDang_ChiTiet);
        txtvNguoiDang=(TextView)findViewById(R.id.textViewNguoiDang_ChiTiet);
        txtvNgayDang=(TextView)findViewById(R.id.textViewNgayDang_ChiTiet);
        txtvDiaChi=(TextView)findViewById(R.id.textViewDiaChi_ChiTiet);
        txtvGia=(TextView)findViewById(R.id.textViewGia_ChiTiet);
        txtvDienTich=(TextView)findViewById(R.id.textViewDienTich_ChiTiet);
        txtvSdt=(TextView)findViewById(R.id.textViewSdt_ChiTiet) ;
        txtvMoTa=(TextView)findViewById(R.id.textViewMoTa_ChiTiet);
        btnLuu=(Button)findViewById(R.id.buttonLuu_ChiTiet);
        btnChat=(Button)findViewById(R.id.btn_chat);
        lvChat=(ListView)findViewById(R.id.lv_chat);
        editTextChat=(EditText)findViewById(R.id.edt_chat);
        adapterChat= new AdapterChat(arrChat,context,R.layout.item_chat);
        lvChat.setAdapter(adapterChat);
        //setListViewHeight(lvChat);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        myMap=googleMap;
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //yeu cau presenter lay du lieu
        presenterXemChiTietNhaTro.getData(idNhanDuoc);

        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(getApplicationContext(), ChiTietBanDoActivity.class);
                intent.putExtra("ChiTiet",idNhanDuoc);
                startActivity(intent);
            }
        });

        // dissable scroll event

        scrollView = (ScrollView) findViewById(R.id.scrollView_ChiTiet);

        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_ChiTiet))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });




    }

    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    @Override
    public void addChatOffline(Chat chat) {
        editTextChat.setText("");
        arrChat.add(0,chat);
        adapterChat.notifyDataSetChanged();
        setListViewHeight(lvChat);
    }

    @Override
    public void SetData(TinDang tinDang,String urlAvatar,String nameUser,ArrayList<Chat> chats) {

        String url = tinDang.getAnh();
        Picasso.get().load(url)
                //.placeholder(R.drawable.ic_photo_camera_black_24dp)
                .error(R.drawable.ic_error_outline_black_24dp)
                .into(img);

        if(!urlAvatar.equals("nonono")){
            Picasso.get().load(urlAvatar)
                    .into(imgAvatar);

        }
        tentk=nameUser;
        txtvNguoiDang.setText(tentk);

        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date=tinDang.getNgaydang().toDate();
        String ngay= sfd.format(date);
        txtvNgayDang.setText(ngay);
        diaChi=tinDang.getChitietdiachi()+", "+tinDang.gettenquanhuyen()+", "+tinDang.gettenthanhpho();
        gia=tinDang.getGia();
        idNguoiDang=tinDang.getIdtaikhoan();
        dientich=tinDang.getDientich();
        txtvDiaChi.setText("Địa chỉ: "+diaChi);
        txtvGia.setText("Giá: "+gia+ " đ");
        txtvDienTich.setText("Diện tích: "+dientich+" mét vuông");
        txtvMoTa.setText("Mô tả: "+tinDang.getMota());
        txtvSdt.setText(tinDang.getDienthoai());
        txtvSdt.setPaintFlags(txtvSdt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        kinhdo=tinDang.getKinhdo();
        vido=tinDang.getVido();

        LatLng bk = new LatLng(Double.parseDouble(vido),Double.parseDouble(kinhdo));
        myMap.addMarker(new MarkerOptions().position(bk)
                .title(diaChi)
        );
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bk,17));
        //set chat
        arrChat.addAll(chats);
        adapterChat.notifyDataSetChanged();
        setListViewHeight(lvChat);
        progressDialog.dismiss();

    }

    @Override
    public void showMess(String mess) {
        Toast.makeText(context,mess, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void dissmissDialog() {
        progressDialog.dismiss();

    }

}

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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuongtran.timtro.R;
import com.example.cuongtran.timtro.view.adapter.AdapterTabHost;
import com.example.cuongtran.timtro.entity.CheckConnection;
import com.example.cuongtran.timtro.entity.Constant;
import com.example.cuongtran.timtro.entity.TinDang;
import com.example.cuongtran.timtro.entity.TinLuu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    TextView txtvten,txtvEmail,txtvSDT,txtvDiaChi;
    String tenTabHost3,emailTabHost3,sdtTabHost3,diachiTabHost3,idTaiKhoan;

    Context mContext;
    Button btnTabHost3;
    Toolbar toolbar;
    TabHost tabhost;
    ImageView imgAvatar;
    TextView textViewTen;
    ListView lv1,lv2;
    ArrayList<TinDang> arr1;
    ArrayList<TinDang> arr2;
    ArrayList<TinLuu> arrTinLuu;
    AdapterTabHost adapterTabHost1, adapterTinLuu;
    ProgressDialog progressDialog;
    Bitmap resizedBitmap = null;
    Uri imgAvatarUri =null;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseStorage mStorage;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    ListenerRegistration listenerRegistration1,listenerRegistration2,listenerRegistration3;
    String link;
    static  int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1 ;
    static int MY_PERMISSIONS_REQUEST_CAMERA = 2 ;
    static int CODE_GALLERY_REQUEST=10;
    static int CODE_CAMERA_REQUEST=20 ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.dang_Xuat_TrangCaNhan) {
            dangXuat();
        }

        return super.onOptionsItemSelected(item);
    }

    private void dangXuat() {
//        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        String uid = mCurrentUser.getUid();
//        Map<String,Object> updates = new HashMap<>();
//        updates.put("token." + refreshedToken, FieldValue.delete());
//
//        firebaseFirestore.collection("taikhoan").document(uid).update(updates);
        mAuth.signOut();
        Toast.makeText(ProfileActivity.this, "Đã Đăng Xuất", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("CUONG","onResume Profile");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("CUONG","onStart Profile");
        getData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("CUONG","onPause Profile");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("CUONG","onRestart Profile");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // huy listenner khi ket thuc activity
        listenerRegistration3.remove();
        listenerRegistration2.remove();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("CUONG","onCreat Profile");
        setContentView(R.layout.activity_profile);
        mContext=this;
        mAuth=FirebaseAuth.getInstance();
        mStorage=FirebaseStorage.getInstance();
        storageReference=mStorage.getReference();
        firebaseFirestore= FirebaseFirestore.getInstance();
        anhxa();
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CheckConnection.haveNetworkConnection(mContext)){

                    CharSequence[] sequence = new CharSequence[]{"Chụp ảnh mới","Chọn Ảnh từ Album"};
                    new AlertDialog.Builder(ProfileActivity.this)
                            .setTitle("Chọn")
                            .setItems(sequence, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(i==0){
                                        dialogInterface.dismiss();
                                        // chup anh
                                        chooseFromCamera();

                                    }
                                    else {
                                        dialogInterface.dismiss();
                                        chooseFromStorage();

                                    }

                                }
                            })
                            .create()
                            .show();

                }else Toast.makeText(mContext, "Kiểm tra lại kết nối internet !", Toast.LENGTH_SHORT).show();







            }
        });

        btnTabHost3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ChinhSuaProfile.class);
                idTaiKhoan=mCurrentUser.getUid();
                intent.putExtra("id",idTaiKhoan);
                intent.putExtra("ten",tenTabHost3);
                intent.putExtra("sdt",sdtTabHost3);
                intent.putExtra("diachi",diachiTabHost3);
                startActivity(intent);
            }
        });










    }

    private void getData() {

        mCurrentUser = mAuth.getCurrentUser();
        if(mCurrentUser!=null){
            // neu da co nguoi dang nhap

            String id =mCurrentUser.getUid();
            // dl tabhost1
            listenerRegistration1=firebaseFirestore.collection(Constant.NHA_TRO).whereEqualTo("idtaikhoan",id)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("CUONG", "Listen failed.", e);
                                return;
                            }

                           arr1.clear();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                TinDang t=doc.toObject(TinDang.class).withId(doc.getId());
                                arr1.add(t);
                            }
                            adapterTabHost1.notifyDataSetChanged();

                        }
                    });


            collectionReference=firebaseFirestore.collection("taikhoan");
            listenerRegistration3 = collectionReference.document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e("CUONG", "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.e("CUONG", "du lieu co su thay doi ");
                        // cap nhat lai
                        textViewTen.setText(snapshot.getString("hoten"));
                            link =snapshot.getString("linkavatar");
                            if(!link.equals("nonono")){
                                Picasso.get().load(link)
                                        .into(imgAvatar);
                            }

                        //dl tabhost 3
                            tenTabHost3=snapshot.getString("hoten");
                            sdtTabHost3=snapshot.getString("sodienthoai") ;
                            diachiTabHost3=snapshot.getString("diachi");
                            emailTabHost3=snapshot.getString("email");
                            if(!sdtTabHost3.equals("nonono")){
                                txtvSDT.setText(sdtTabHost3);

                            }
                            if(!diachiTabHost3.equals("nonono")){
                                txtvDiaChi.setText(diachiTabHost3);
                            }
                            txtvten.setText(tenTabHost3);
                            txtvEmail.setText(emailTabHost3);


                    } else {
                        Log.e("CUONG", "Current data: null");
                    }
                }
            });

            // du lieu tabhost 2
            listenerRegistration2= firebaseFirestore.collection(Constant.TIN_LUU)
                    .whereEqualTo(Constant.TIN_LUU_IDTK, id)
                    //
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.e("CUONG", "Listen failed.", e);
                                return;
                            }


                            arrTinLuu.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                //Log.e("CUONG","dang chay vao day");
                                //Log.e("CUONG","gia tri la "+ doc.getData().toString());
                                TinLuu t = doc.toObject(TinLuu.class).withId(doc.getId());
                                arrTinLuu.add(t);

                            }
                            arr2.clear();
                            for(TinLuu s:arrTinLuu){
                                final String idNha= s.getIdtin();
                                firebaseFirestore.collection(Constant.NHA_TRO)
                                        .document(idNha).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        //in here
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                TinDang tinDang =document.toObject(TinDang.class).withId(idNha);
                                                arr2.add(tinDang);
                                                adapterTinLuu.notifyDataSetChanged();
                                            } else {
                                                Toast.makeText(mContext, "Tin đã bị xóa", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        } else {
                                            Log.d("CUONG", "get failed with ", task.getException());
                                        }

                                        //in here
                                    }
                                });


                            }
                            adapterTinLuu.notifyDataSetChanged();


                            //Log.e("CUONG", "List Thong bao " + arrayList.get(0).getIdNhaTro());
                        }
                    });


        }else {
            // chua co nguoi dang nhap
            textViewTen.setText("Đăng Nhập");
        }




    }

    private void chooseFromCamera() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // kiem tra permission
            if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                //yeu cau cap quyen

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                        Manifest.permission.CAMERA)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(ProfileActivity.this)
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

                                    ActivityCompat.requestPermissions(ProfileActivity.this,
                                            new String[]{Manifest.permission.CAMERA},
                                            MY_PERMISSIONS_REQUEST_CAMERA);


                                }
                            })
                            .create()
                            .show();

                } else {

                    // No explanation needed; request the permission

                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }

            }
            else {
                //permission da cho phep
                setAvatarFromCamera();


            }


        }
        else {
            setAvatarFromCamera();

        }




    }



    private  void chooseFromStorage(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // kiem tra permission
            if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                //yeu cau cap quyen

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(ProfileActivity.this)
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

                                    ActivityCompat.requestPermissions(ProfileActivity.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


                                }
                            })
                            .create()
                            .show();

                } else {

                    // No explanation needed; request the permission

                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }

            }
            else {
                //permission da cho phep
                setAvatarFromStorage();


            }


        }
        else {
            setAvatarFromStorage();


        }


    }

    private void setAvatarFromCamera() {
        //Toast.makeText(mContext, "Da cho phep", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent,CODE_CAMERA_REQUEST);

    }

    private void setAvatarFromStorage() {
        //Toast.makeText(mContext, "Da cho phep", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        this.startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh"),CODE_GALLERY_REQUEST);

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
                int b2 = b * 400 / a;
                resizedBitmap = Bitmap.createScaledBitmap(
                        bitmap, 400, b2, false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
                // Set avatar hinh tron
//                RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(),resizedBitmap);
//                roundedBitmapDrawable.setCircular(true);
                imgAvatar.setImageBitmap(resizedBitmap);
                // dua len fireBase
            progressDialog= new ProgressDialog(mContext);
            progressDialog.setMessage("Đang cập nhật...");
            progressDialog.show();

            Calendar calendar= Calendar.getInstance();
            StorageReference img_ref= storageReference.child("avatar").child("avatar"+ calendar.getTimeInMillis()+".PNG");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data2 = baos.toByteArray();

            UploadTask uploadTask = img_ref.putBytes(data2);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    progressDialog.dismiss();
                    Toast.makeText(mContext, exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    //Thêm vào data base
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("linkavatar", downloadUrl.toString());

                    firebaseFirestore.collection("taikhoan").document(mAuth.getCurrentUser().getUid())
                            .update("linkavatar",downloadUrl.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "Cập nhật avatar thành công", Toast.LENGTH_SHORT).show();

                        }
                    });
                    // Xoa avatar cu di
                    if(!link.equals("nonono")){
                        StorageReference photoRef = mStorage.getReferenceFromUrl(link);
                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                Log.d("CUONG", "onSuccess: deleted file");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                Log.d("CUONG", "onFailure: did not delete file");
                            }
                        });

                    }


                }
            });




        }

        if ((requestCode == CODE_CAMERA_REQUEST) && (resultCode == RESULT_OK) && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            int a = bitmap.getWidth();
            int b = bitmap.getHeight();
            int b2 = b * 400 / a;
            resizedBitmap = Bitmap.createScaledBitmap(
                    bitmap, 400, b2, false);
                imgAvatar.setImageBitmap(resizedBitmap);

                //up load len fireBase
            progressDialog= new ProgressDialog(mContext);
            progressDialog.setMessage("Đang cập nhật...");
            progressDialog.show();
            Calendar calendar= Calendar.getInstance();
            StorageReference img_ref= storageReference.child("avatar").child("avatar"+ calendar.getTimeInMillis()+".PNG");

            //imageView.setDrawingCacheEnabled(true);
            //imageView.buildDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data2 = baos.toByteArray();

            UploadTask uploadTask = img_ref.putBytes(data2);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    progressDialog.dismiss();
                    Toast.makeText(mContext, exception.toString(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    progressDialog.dismiss();
                    Toast.makeText(mContext, "Cập nhật avatar thành công", Toast.LENGTH_SHORT).show();
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    firebaseFirestore.collection("taikhoan").document(mAuth.getCurrentUser().getUid())
                            .update("linkavatar",downloadUrl.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "Cập nhật avatar thành công", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });


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
                    setAvatarFromStorage();

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
                    setAvatarFromCamera();

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
        imgAvatar=(ImageView)findViewById(R.id.img_avatar_profile);
        textViewTen=(TextView)findViewById(R.id.txtv_HoTen_Profile) ;
        toolbar=(Toolbar)findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        //btnLogOut=(Button)findViewById(R.id.btnlogOut);
        tabhost= (TabHost)findViewById(R.id.tabhost_Profile);
        tabhost.setup();
        //tao tab 1 va gan id
        TabHost.TabSpec tab1 = tabhost.newTabSpec("t1");
        TabHost.TabSpec tab2 = tabhost.newTabSpec("t2");
        TabHost.TabSpec tab3 = tabhost.newTabSpec("t3");
        tab1.setIndicator("Tin đăng");
        tab2.setIndicator("Tin lưu");
        tab3.setIndicator("Profile");
        tab1.setContent(R.id.tabTinDang_profile);
        tab2.setContent(R.id.tabTinLuu_profile);
        tab3.setContent(R.id.tabThongTinCaNhan_profile);
        tabhost.addTab(tab1);
        tabhost.addTab(tab2);
        tabhost.addTab(tab3);
        // anh xa lv
        lv1=(ListView)findViewById(R.id.lv_tinDang_tabHost1);
        lv2=(ListView)findViewById(R.id.lv_tinLuu_tabHost2);
        arr1=new ArrayList<TinDang>();
        arr2=new ArrayList<TinDang>();
        arrTinLuu= new ArrayList<TinLuu>();
        adapterTabHost1=new AdapterTabHost(getApplicationContext(),R.layout.itemtabhost,arr1);
        adapterTinLuu=new AdapterTabHost(getApplicationContext(),R.layout.itemtabhost,arr2);
        lv1.setAdapter(adapterTabHost1);
        lv2.setAdapter(adapterTinLuu);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ProfileActivity.this, "tab Host 1", Toast.LENGTH_SHORT).show();
                //Chuyen sang chi tiet
                Intent intent = new Intent(mContext,XemChiTietNhaTroActivity.class);
                intent.putExtra("CUONG",arr1.get(i).id);
                startActivity(intent);
            }
        });
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int vitri=i ;
                // Show dialog

                CharSequence[] s = {"Xóa", "Chỉnh sửa"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Lựa Chọn");
                builder.setItems(s, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            // Xóa tin đăng

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.Theme_AppCompat_Light_Dialog_Alert);
                            builder.setMessage("Xác nhận xóa ?")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // đồng ý xóa
                                            xoaTinDang(arr1.get(vitri).id);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // cancel
                                        }
                                    }).show();



//

                        } else {
                            // Chuyển sang activity Chỉnh sửa
                            Intent intent = new Intent(mContext,SuaTin.class);
                            intent.putExtra("SUA",arr1.get(vitri).id);

                            startActivity(intent);


                        }

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });

        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ProfileActivity.this, "tab Host 2", Toast.LENGTH_SHORT).show();
                //Chuyen sang chi tiet
                Intent intent = new Intent(mContext,XemChiTietNhaTroActivity.class);
                intent.putExtra("CUONG",arr2.get(i).id);
                startActivity(intent);

            }
        });
        lv2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //show Dialog
                final int vitri=i;

                CharSequence[] s = {"Xóa"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Lựa Chọn");
                builder.setItems(s, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            // Xóa tin lưu

                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,R.style.Theme_AppCompat_Light_Dialog_Alert);
                            builder.setMessage("Xác nhận xóa ?")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // đồng ý xóa
                                            xoaTinLuu(arrTinLuu.get(vitri).id);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // cancel
                                        }
                                    }).show();
//

                        }

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                return true;
            }
        });
//Anh Xa tab host 3
        txtvten=(TextView)findViewById(R.id.ten_TabHost3);
        txtvEmail=(TextView)findViewById(R.id.email_TabHost3);
        txtvSDT=(TextView)findViewById(R.id.sdt_TabHost3);
        txtvDiaChi=(TextView)findViewById(R.id.diaChi_TabHost3);
        btnTabHost3=(Button)findViewById(R.id.btn_tabHost3);


    }

    private void xoaTinLuu(String id) {
        Log.e("CUONG","ma tin bi xoa "+ id);
        firebaseFirestore.collection(Constant.TIN_LUU).document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext, "Đã xóa", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CUONG", "Error deleting document", e);
                    }
                });

    }

    private void xoaTinDang(String id) {
        Log.e("CUONG","ma tin bi xoa "+ id);
        firebaseFirestore.collection(Constant.NHA_TRO).document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext, "Đã xóa", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CUONG", "Error deleting document", e);
                    }
                });

    }
}

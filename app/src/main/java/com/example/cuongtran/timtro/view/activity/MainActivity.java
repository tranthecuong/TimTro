package com.example.cuongtran.timtro.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;

import com.example.cuongtran.timtro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private XemTinFragment manHinh1 = new XemTinFragment();
    private XemTinMapFragment xemTinMapFragment = new XemTinMapFragment();
    private DangKyThongBaoFragment dangKyThongBaoFragment = new DangKyThongBaoFragment();
    private XemThongBaoFragment xemThongBaoFragment = new XemThongBaoFragment();
    private ManageProfileFragment manHinh5 = new ManageProfileFragment();
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private Button btnToolBar;
    private int currentTab = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnToolBar = (Button) findViewById(R.id.btn_toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcon();

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        toolbar.setTitle("Tìm trọ");
                        btnToolBar.setVisibility(View.VISIBLE);
                        btnToolBar.setBackgroundResource(R.drawable.filter_outline_white);
                        currentTab = 1;
                        break;
                    case 1:
                        toolbar.setTitle("Phòng trọ quanh đây");
                        btnToolBar.setVisibility(View.VISIBLE);
                        btnToolBar.setBackgroundResource(R.drawable.ic_autorenew_black_24dp);
                        currentTab = 2;
                        break;
                    case 2:
                        toolbar.setTitle("Đăng kí nhận thông báo");
                        btnToolBar.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        toolbar.setTitle("Thông báo");
                        btnToolBar.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        toolbar.setTitle("Tài khoản");
                        btnToolBar.setVisibility(View.INVISIBLE);
                        break;

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CUONG", String.valueOf(currentTab));
                if (currentTab == 1) {
                    Intent intent = new Intent(MainActivity.this, LocTinActivity.class);
                    startActivity(intent);
                    //startActivityForResult(intent, 200);

                } else {
                    if (currentTab == 2) {
                        xemTinMapFragment.getduLieu();
                    }
                }

            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 200) {
//            FilterPacket packet = data.getParcelableExtra("filter");
//            boolean action = packet.getRefreshNeed();
//            if(action==true){
//                // cap nhat lai man hinh 1
//                // manHinh1.updateFilter(packet);
//            }
//
//        }
//    }


    private void setupTabIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_query_builder_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_place_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_mail_outline_black_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_none_black_24dp);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_person_outline_black_24dp);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0: {
                    return manHinh1;
                }
                case 1: {
                    return xemTinMapFragment;
                }
                case 2: {
                    return dangKyThongBaoFragment;
                }
                case 3: {
                    return xemThongBaoFragment;
                }
                case 4: {

                    return manHinh5;
                }


            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "1";
//                case 1:
//                    return "2";
//                case 2:
//                    return "3";
//                case 3:
//                    return "4";
//                case 4:
//                    return "5";
//            }
            return null;
        }

    }



}

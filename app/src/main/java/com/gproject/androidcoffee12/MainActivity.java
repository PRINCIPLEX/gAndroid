package com.gproject.androidcoffee12;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.*;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.gproject.MySqlData;
import com.gproject.entity.CustomerEntity;
import com.gproject.fragment.BookFragment;
import com.gproject.fragment.OrderFragment;
import com.gproject.fragment.UserFragment;


import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION;


public class MainActivity extends AppCompatActivity {

    List<String> mPermissionList = new ArrayList<>();
    private static final int MY_PERMISSIONS_REQUEST_CODE = 100;
    SmartTabLayout smartTabLayout;
    ViewPager viewPager;
    String[] camera = new String[]{Manifest.permission.CAMERA};
    boolean isExist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getPermission();
        isExist = ((MySqlData)getApplication()).isFloatExist();

        getSupportActionBar().setTitle("石大咖啡");

        //边框阴影权重0
        getSupportActionBar().setElevation(0);
        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewPager);
        FragmentPagerItems tabs = FragmentPagerItems.with(this)
                .add("点餐", BookFragment.class)
                .add("订单", OrderFragment.class)
                .add("我的", UserFragment.class)
                .create();

        //FragmentAdapter
        FragmentPagerAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), tabs);
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
        getPermission();

        //float是否存在，避免付款后返回首页再创建
        if(!isExist)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, camera, 100);
            }
            else if (!Settings.canDrawOverlays(this)) {
                //引导用户去开启权限
                Intent intent = new Intent(ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }

            else {
                ((MySqlData) getApplication()).createFloatView();
                ((MySqlData)getApplication()).setFloatExist(true);
                isExist=true;
            }
        }

    }


    public void getPermission() {

        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.RECORD_AUDIO
        };


        mPermissionList.clear();                                    //清空已经允许的没有通过的权限
        for (int i = 0; i < permissions.length; i++) {          //逐个判断是否还有未通过的权限
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

        if (mPermissionList.size() > 0) {                           //有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_CODE);
        }

    }

}


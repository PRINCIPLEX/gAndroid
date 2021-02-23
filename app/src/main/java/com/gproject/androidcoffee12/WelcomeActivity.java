package com.gproject.androidcoffee12;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;
import com.gproject.MySqlData;
import com.gproject.entity.CustomerEntity;

import java.util.Timer;
import java.util.TimerTask;


public class WelcomeActivity extends Activity {


    boolean isFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        String cus  = sp.getString("customerEntity",null);
        if(cus!=null)
        {
            CustomerEntity customerEntity = new Gson().fromJson(cus, CustomerEntity.class);
            ((MySqlData)getApplication()).setCustomerEntity(customerEntity);
            ((MySqlData)getApplication()).readCustomer();
            isFirst=false;
        }
        else
        {
            isFirst=true;
        }
        startMainActivity();

    }

    private void startMainActivity() {
        TimerTask delayTask = new TimerTask() {
            @Override
            public void run() {
                if(isFirst)
                {
                    Intent mainIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                }
                else
                {
                    Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(delayTask,2000);//延时两秒执行 run 里面的操作
    }



}
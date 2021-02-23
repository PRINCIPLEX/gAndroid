package com.gproject.androidcoffee12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ShopCloseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close);
        getSupportActionBar().setTitle("打烊通知");


        Button btn_back = findViewById(R.id.btn_backtomain);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(ShopCloseActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
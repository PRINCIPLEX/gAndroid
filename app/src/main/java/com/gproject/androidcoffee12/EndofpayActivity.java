package com.gproject.androidcoffee12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EndofpayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endofpay);
        getSupportActionBar().setTitle("支付成功");


        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(EndofpayActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}

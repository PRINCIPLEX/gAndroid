package com.gproject.androidcoffee12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.gproject.MySqlData;

public class RefreshActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        getSupportActionBar().setTitle("重新加载");


        Button btn_back = findViewById(R.id.btn_refresh);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MySqlData)getApplicationContext()).readCustomer();
                Intent bookIntent = new Intent(RefreshActivity.this,BookActivity.class);
                startActivity(bookIntent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}
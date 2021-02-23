package com.gproject.androidcoffee12;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gproject.MySqlData;
import com.gproject.adapter.OrderAdapter;
import com.gproject.entity.OrderEntity;
import okhttp3.*;

import java.io.IOException;
import java.util.List;

public class ChangUserNameActivity  extends AppCompatActivity {

    String URL;
    String uphone;
    String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        getSupportActionBar().setTitle("更改用户名");
        URL = ((MySqlData)getApplicationContext()).getURL();
        uphone = ((MySqlData)getApplicationContext()).getCustomerEntity().getPhone();

        TextView username = (TextView)findViewById(R.id.edit_content);
        TextView save = (TextView) findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = username.getText().toString();
             //   ((MySqlData)getApplicationContext()).setUserName(userName);
                changeUserName();

            }
        });


    }

    public void changeUserName(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(URL+"change_user_name"+"/"+uphone+"/"+userName)
                .build();
        Call call = client.newCall(request);

        //异步调用,并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ChangUserNameActivity.this,"哎呀，网络好像有点问题",Toast.LENGTH_SHORT).show();
                Log.e("yztest", "yzChangeUserNameFail");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MySqlData)getApplicationContext()).getCustomerEntity().setUsername(userName);
                        ((MySqlData)getApplicationContext()).reWriteShared();
                        Toast.makeText(ChangUserNameActivity.this,"更改用户名成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

}


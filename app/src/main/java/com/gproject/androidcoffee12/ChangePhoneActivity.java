package com.gproject.androidcoffee12;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.gproject.MySqlData;
import okhttp3.*;

import java.io.IOException;

public class ChangePhoneActivity extends AppCompatActivity {
    String URL;
    String uphone;
    String newPhone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        getSupportActionBar().setTitle("更改手机");


        URL = ((MySqlData)getApplicationContext()).getURL();
        uphone = ((MySqlData)getApplicationContext()).getCustomerEntity().getPhone();

        TextView phone = (TextView)findViewById(R.id.edit_content);
        TextView save = (TextView) findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPhone = phone.getText().toString();
                if(newPhone.length()!=11)
                {
                    Toast.makeText(ChangePhoneActivity.this,"手机号码有误",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    changeUserPhone();

                }
            }
        });

    }

    public void changeUserPhone(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(URL+"change_user_phone"+"/"+uphone+"/"+newPhone)
                .build();
        Call call = client.newCall(request);

        //异步调用,并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ChangePhoneActivity.this,"哎呀，网络好像有点问题",Toast.LENGTH_SHORT).show();
                Log.e("yztest", "yzChangeUserPhoneFail");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String str = response.body().string();
                if(str.equals("501"))
                {
                    Looper.prepare();
                    Toast.makeText(ChangePhoneActivity.this,"该手机号已被使用",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MySqlData)getApplicationContext()).getCustomerEntity().setPhone(newPhone);
                            ((MySqlData)getApplicationContext()).reWriteShared();
                            ((MySqlData)getApplicationContext()).closeConnect();
                            ((MySqlData)getApplicationContext()).readCustomer();
                            Toast.makeText(ChangePhoneActivity.this,"更新手机号成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }

}


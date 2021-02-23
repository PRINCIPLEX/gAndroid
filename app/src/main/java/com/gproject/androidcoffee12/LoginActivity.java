package com.gproject.androidcoffee12;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.gproject.MySqlData;
import com.gproject.entity.CustomerEntity;
import okhttp3.*;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity {

    String URL;
    String account;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        URL = ((MySqlData) getApplication()).getURL();
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("登录");

        TextView tv_account = (TextView) findViewById(R.id.et_user_account);
        TextView tv_password = (TextView) findViewById(R.id.et_user_password);
        TextView tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        TextView tvRegister = (TextView) findViewById(R.id.tv_go_to_register);
        Button btnLogin = (Button) findViewById(R.id.btn_login);

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(forgetIntent);
                finish();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = tv_account.getText().toString();
                password = tv_password.getText().toString();
                if (account.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();

                } else {
                    if (password.equals("")) {
                        Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    } else {
                        login();
                    }
                }

            }
        });

    }

    public void login() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(URL + "login" + "/" + account + "/" + password)
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "哎呀，网络好像有点问题", Toast.LENGTH_SHORT).show();
                Looper.loop();
                Log.e("yztest", "yzLoginFail");
                ;
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Gson gson = new Gson();
                String str = response.body().string();
                if (str.equals("500")) {
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this, "账号或密码出错", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomerEntity customerEntity = gson.fromJson(str, CustomerEntity.class);
                            ((MySqlData) getApplication()).setCustomerEntity(customerEntity);

                            //写入安卓机
                            SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
                            //这个的话是用来写文件的
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("customerEntity", str);
                            //写完我们的文件的话记得要将我们的文件进行提交
                            editor.apply();

                            //steamId remoteURL rtmpURL getPrivacy initWebSocket
                            ((MySqlData) getApplication()).readCustomer();
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
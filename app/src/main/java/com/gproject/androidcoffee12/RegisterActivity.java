package com.gproject.androidcoffee12;

import android.content.Intent;
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

public class RegisterActivity  extends AppCompatActivity {
    String phone;
    String userName;
    String email;
    String password;
    String passwordAgain;
    String URL;
    TextView edPhone;
    TextView edUserName;
    TextView edEmail;
    TextView edPassword;
    TextView edPasswordAgain;





    CustomerEntity customerEntity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("注册");

        URL = ((MySqlData)getApplication()).getURL();
        edPhone = (TextView)findViewById(R.id.edit_phone);
        edUserName = (TextView)findViewById(R.id.edit_user_name);
        edEmail = (TextView)findViewById(R.id.edit_email);
        edPassword = (TextView)findViewById(R.id.edit_password);
        edPasswordAgain = (TextView)findViewById(R.id.edit_password_again);
        Button register  = (Button)findViewById(R.id.btn_register);
        TextView tv_forget_password = (TextView)findViewById(R.id.tv_forget_password);
        TextView tv_login = (TextView)findViewById(R.id.tv_go_to_login);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = edPhone.getText().toString();
                userName = edUserName.getText().toString();
                email = edEmail.getText().toString();
                password = edPassword.getText().toString();
                passwordAgain = edPasswordAgain.getText().toString();
                if(phone.equals("")||phone.length()!=11)
                {
                    Toast.makeText(RegisterActivity.this,"手机号码不正确",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    if(userName.equals(""))
                    {
                        Toast.makeText(RegisterActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(email.equals("")||email.indexOf(".com")==-1)
                        {
                            Toast.makeText(RegisterActivity.this,"请输入正确的邮箱地址",Toast.LENGTH_SHORT).show();

                        }

                        else
                        {
                            if(password.equals(""))
                            {
                                Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                if(!passwordAgain.equals(password))
                                {
                                    Toast.makeText(RegisterActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    customerEntity = new CustomerEntity();
                                    customerEntity.setPhone(phone);
                                    customerEntity.setUsername(userName);
                                    customerEntity.setEmail(email);
                                    customerEntity.setPassword(password);
                                    registerFunction();
                                }
                            }
                        }
                    }
                }

            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetIntent = new Intent(RegisterActivity.this, ForgetPasswordActivity.class);
                startActivity(forgetIntent);
                finish();
            }
        });

    }

    //formbody push
    public void registerFunction()
    {
        Gson gson = new Gson();
        String customer = gson.toJson(customerEntity);
        FormBody formBody = new FormBody.Builder()
                .add("customerEntity", customer)
                .build();


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(formBody)
                .url(URL+"android_register")
                .build();
        Call call = client.newCall(request);

        //异步调用,并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Looper.loop();
                Toast.makeText(RegisterActivity.this, "哎呀，网络好像有点问题", Toast.LENGTH_SHORT).show();
                Looper.prepare();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String str  = response.body().string();
                String log;
                if(str.equals("501"))
                {
                    log = "手机号码已经被使用了哟";
                }
                else
                {
                    log="注册成功!";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            edPassword.setText("");
                            edPasswordAgain.setText("");

                        }
                    });
                }
                Looper.prepare();
                Toast.makeText(RegisterActivity.this, log, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

}

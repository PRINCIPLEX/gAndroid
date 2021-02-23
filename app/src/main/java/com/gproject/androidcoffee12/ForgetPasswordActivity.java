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
import com.gproject.MySqlData;
import okhttp3.*;

import java.io.IOException;

public class ForgetPasswordActivity extends AppCompatActivity {

    String tvPhone;
    String tvEmail;
    String URL;
    String newPassword;
    TextView newPassword1;
    TextView newPassword2;
    TextView tv_register;
    TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().setTitle("忘记密码");

        URL = ((MySqlData) getApplication()).getURL();
        TextView tv_editPhone = (TextView) findViewById(R.id.edit_phone);
        TextView tv_email = (TextView) findViewById(R.id.edit_email);
        newPassword1 = (TextView) findViewById(R.id.edit_password);
        newPassword2 = (TextView) findViewById(R.id.edit_password_again);
        Button save = (Button) findViewById(R.id.btn_savePass);
        tv_register = (TextView)findViewById(R.id.tv_register);
        tv_login = (TextView)findViewById(R.id.tv_go_to_login);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvPhone = tv_editPhone.getText().toString();
                tvEmail = tv_email.getText().toString();
                newPassword = newPassword1.getText().toString();
                String newPasswordAgain = newPassword2.getText().toString();

                if (tvPhone.equals("") || tvPhone.length() != 11) {
                    Toast.makeText(ForgetPasswordActivity.this, "手机号码不正确", Toast.LENGTH_SHORT).show();

                } else {
                    if (tvEmail.equals("")) {
                        Toast.makeText(ForgetPasswordActivity.this, "验证邮箱不能为空", Toast.LENGTH_SHORT).show();

                    } else {
                        if (newPassword.equals("")) {
                            Toast.makeText(ForgetPasswordActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();

                        } else {
                            if (!newPassword.equals(newPasswordAgain)) {
                                Toast.makeText(ForgetPasswordActivity.this, "新密码不一致", Toast.LENGTH_SHORT).show();
                            } else {
                                forgetPassword();
                            }
                        }
                    }
                }
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(ForgetPasswordActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent =  new Intent(ForgetPasswordActivity.this,LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }


    public void forgetPassword() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(URL + "forget_password" + "/" + tvPhone + "/" + tvEmail + "/" + newPassword)
                .build();
        Call call = client.newCall(request);

        //异步调用,并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ForgetPasswordActivity.this, "哎呀，网络好像有点问题", Toast.LENGTH_SHORT).show();
                Log.e("yztest", "yzForgetUserPasswordFail");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                String str = response.body().string();
                if (str.equals("501")) {
                    Looper.prepare();
                    Toast.makeText(ForgetPasswordActivity.this, "验证邮箱不对哟", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgetPasswordActivity.this, "更新密码成功", Toast.LENGTH_SHORT).show();
                            newPassword1.setText("");
                            newPassword2.setText("");
                        }
                    });
                }
            }
        });
    }

}


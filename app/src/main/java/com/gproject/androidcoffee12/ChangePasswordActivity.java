package com.gproject.androidcoffee12;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.gproject.MySqlData;
import okhttp3.*;

import java.io.IOException;

public class ChangePasswordActivity extends AppCompatActivity {

    String URL;
    String uphone;
    String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_password);
        getSupportActionBar().setTitle("更改密码");

        URL = ((MySqlData)getApplicationContext()).getURL();
        uphone = ((MySqlData)getApplicationContext()).getCustomerEntity().getPhone();



        TextView oldPassword = (TextView)findViewById(R.id.edit_old_password);
        TextView newPassword1 = (TextView) findViewById(R.id.edit_password);
        TextView newPassword2 = (TextView) findViewById(R.id.edit_password_again);
        Button save = (Button)findViewById(R.id.btn_register);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String realPassword =  ((MySqlData)getApplicationContext()).getCustomerEntity().getPassword();
                String tvOldPassword = oldPassword.getText().toString();
                newPassword =  newPassword1.getText().toString();
                String newPasswordAgain = newPassword2.getText().toString();


                if(!realPassword.equals(tvOldPassword))
                {
                    Toast.makeText(ChangePasswordActivity.this,"旧密码错误",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(newPassword.equals(""))
                    {
                        Toast.makeText(ChangePasswordActivity.this,"请输入新密码",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        if(!newPassword.equals(newPasswordAgain))
                        {
                            Toast.makeText(ChangePasswordActivity.this,"新密码不一致",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            changeUserPassword();
                        }
                    }

                }
            }


        });

    }



    public void changeUserPassword(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(URL+"change_user_password"+"/"+uphone+"/"+newPassword)
                .build();
        Call call = client.newCall(request);

        //异步调用,并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ChangePasswordActivity.this,"哎呀，网络好像有点问题",Toast.LENGTH_SHORT).show();
                Log.e("yztest", "yzChangeUserPasswordFail");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MySqlData)getApplicationContext()).getCustomerEntity().setPassword(newPassword);
                        ((MySqlData)getApplicationContext()).reWriteShared();
                        Toast.makeText(ChangePasswordActivity.this,"更新密码成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

}


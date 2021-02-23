package com.gproject.androidcoffee12;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gproject.MySqlData;
import com.gproject.adapter.SettleCoffeeAdapter;
import com.gproject.entity.LinkOrderEntity;
import com.gproject.entity.OrderEntity;
import com.gproject.entity.ProductListEntity;
import okhttp3.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    String URL;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String selectText = "支付宝支付";
    OrderEntity entity;
    List<LinkOrderEntity> linkOrderEntityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setTitle("支付");

        URL=  ((MySqlData)getApplicationContext()).getURL()+"submitorder";


        TextView tv_price = findViewById(R.id.tv_price);
        Button btn_payment = findViewById(R.id.btn_payment);
        radioGroup = findViewById(R.id.rg_payment);
        radioButton = findViewById(radioGroup.getCheckedRadioButtonId());



        //获取订单信息
        entity = (OrderEntity) getIntent().getSerializableExtra("order");
        linkOrderEntityList=(List<LinkOrderEntity>)getIntent().getSerializableExtra("link");


        tv_price.setText(String.valueOf(entity.getTotalprice()));



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRadioBtn();
            }
        });

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entity.setPayway(selectText);
                request();

            }
        });


    }


    private void selectRadioBtn(){
        radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        selectText = radioButton.getText().toString();
    }

    public void request() {

        Gson gson = new Gson();
        String order = gson.toJson(entity);
        String link =gson.toJson(linkOrderEntityList);


        FormBody formBody = new FormBody.Builder()
                .add("order", order)
                .add("link", link)
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(formBody)
                .url(URL)
                .build();
        Call call = client.newCall(request);

        //异步调用,并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.loop();
                Toast.makeText(PaymentActivity.this, "哎呀，网络好像有点问题", Toast.LENGTH_SHORT).show();
                Looper.prepare();
                Log.e("yztest", "yzpaymentFail");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String str = response.body().string();
                if(str.equals("0"))
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent shopClose = new Intent(PaymentActivity.this,ShopCloseActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(shopClose);
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PaymentActivity.this,"订单提交完成",Toast.LENGTH_SHORT);
                            Intent endofpay = new Intent(PaymentActivity.this,EndofpayActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(endofpay);
                        }
                    });
                }

            }
        });

    }

}

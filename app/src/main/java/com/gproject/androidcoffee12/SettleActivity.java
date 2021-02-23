package com.gproject.androidcoffee12;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gproject.MySqlData;
import com.gproject.adapter.SettleCoffeeAdapter;
import com.gproject.entity.CustomerEntity;
import com.gproject.entity.LinkOrderEntity;
import com.gproject.entity.OrderEntity;
import com.gproject.entity.ProductListEntity;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle);
        getSupportActionBar().setTitle("提交订单");
        TextView originPrice = findViewById(R.id.txt_origin_price);
        TextView totalPrice  = findViewById(R.id.txt_total_price);
        TextView username = findViewById(R.id.tv_username);
        TextView userphone = findViewById(R.id.tv_userphone);
        EditText remark = findViewById(R.id.tv_remark);


        Button btnSubmitOrder = findViewById(R.id.btn_submit_order);
        CheckBox cbDb = findViewById(R.id.cbdb);
        //获取商品
       List<ProductListEntity.ProductEntity> productEntities = (List<ProductListEntity.ProductEntity>) getIntent().getSerializableExtra("shopCart");

        //顾客、手机号码
        MySqlData mySqlData = (MySqlData)getApplication();
        CustomerEntity customerEntity = mySqlData.getCustomerEntity();
        String customerName = customerEntity.getUsername();
        String phone = customerEntity.getPhone();
        username.setText(customerName);
        userphone.setText(phone);

        //原价
        String price =getIntent().getStringExtra("totalPrice");
        originPrice.setText("原价¥" + price);
        //打包价
        boolean db =getIntent().getBooleanExtra("db",true);
        double op =Double.parseDouble(price) + 3;;


        ///RecycleView
        RecyclerView recyclerView = findViewById(R.id.book_view);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SettleCoffeeAdapter adapter = new SettleCoffeeAdapter((productEntities));
        recyclerView.setAdapter(adapter);


        cbDb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    totalPrice.setText("¥"+op);
                }
                else
                {
                    totalPrice.setText("¥"+price);

                }
            }
        });


        //初始化界面
        if(db)
        {
            cbDb.setChecked(true);

        }
        else
        {
            //默认是不选的，所以要多加一行
            cbDb.setChecked(false);
            totalPrice.setText("¥"+price);
        }


        //跳转支付
        btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setCphone(phone);
                orderEntity.setCname(customerName);
                if(cbDb.isChecked())
                {
                    orderEntity.setIseatway(true);
                    orderEntity.setEatway("打包");
                    orderEntity.setTotalprice(op);
                }
                else
                {
                    orderEntity.setIseatway(false);
                    orderEntity.setEatway("堂喝");
                    orderEntity.setTotalprice(Double.parseDouble(price));
                }

                //备注
                orderEntity.setRemark(remark.getText().toString());




                //当前时间
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// HH:mm:ss
                //订单时间
                Date date = new Date(System.currentTimeMillis());
                orderEntity.setTime(date);
                //订单id
                String orderId = "cup"+simpleDateFormat.format(date);
                orderEntity.setOrderid(orderId);

                List<LinkOrderEntity>linkOrderEntityList = new ArrayList<>();
                for(int i=0;i<productEntities.size();i++)
                {
                    LinkOrderEntity linkOrderEntity = new LinkOrderEntity();
                    linkOrderEntity.setCount(productEntities.get(i).getProductCount());
                    linkOrderEntity.setGid(productEntities.get(i).getProductId());
                    linkOrderEntity.setUid(orderId);
                    linkOrderEntityList.add(linkOrderEntity);
                }

                //订单传值
                Intent paymentIntent = new Intent(SettleActivity.this,PaymentActivity.class);
                paymentIntent.putExtra("order", (Serializable)orderEntity);
                paymentIntent.putExtra("link", (Serializable)linkOrderEntityList);
                //商品
                startActivity(paymentIntent);
            }
        });

    }

}

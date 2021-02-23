package com.gproject.androidcoffee12;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gproject.adapter.SettleCoffeeAdapter;
import com.gproject.entity.OrderEntity;
import com.gproject.entity.ProductListEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    TextView db;
    TextView totalPrice;
    TextView tvUser;
    TextView tvPhone;
    TextView tvRemark;
    TextView tvOrderNum;
    TextView tvCreateAt;
    TextView tvPayMethod;
    RecyclerView bookView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_show);
        getSupportActionBar().setTitle("订单");


        totalPrice = (TextView)findViewById(R.id.txt_total_price);
        db=(TextView)findViewById(R.id.tv_db2);
        tvUser = (TextView)findViewById(R.id.tv_user);
        tvPhone= (TextView)findViewById(R.id.tv_phone);
        tvRemark= (TextView)findViewById(R.id.txt_remark);
        tvOrderNum= (TextView)findViewById(R.id.txt_order_num);
        tvCreateAt= (TextView)findViewById(R.id.txt_created_at);
        tvPayMethod= (TextView)findViewById(R.id.txt_pay_method);
        bookView = (RecyclerView)findViewById(R.id.book_view);



        OrderEntity orderList = (OrderEntity)getIntent().getSerializableExtra("orderList");
        if(orderList!=null)
        {
            List<ProductListEntity.ProductEntity> productEntities = orderList.getProductEntities();
            if(orderList.isIseatway())
            {
                db.setVisibility(View.VISIBLE);
            }

            totalPrice.setText(String.valueOf(orderList.getTotalprice()));
            //订单信息栏
            tvUser.setText(orderList.getCname());
            tvPhone.setText(orderList.getCphone());
            tvRemark.setText(orderList.getRemark());
            tvOrderNum.setText(String.valueOf(orderList.getOrderid()));


            //订单时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
            Date date = orderList.getTime();
            tvCreateAt.setText(simpleDateFormat.format(date));


            //订单id


            tvPayMethod.setText(orderList.getPayway());

            //recyclerView 用的是settleCoffeeAdapter
            LinearLayoutManager layoutManager= new LinearLayoutManager(this);
            bookView.setLayoutManager(layoutManager);
            SettleCoffeeAdapter adapter = new SettleCoffeeAdapter((orderList.getProductEntities()));
            bookView.setAdapter(adapter);
        }
    }
}

package com.gproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gproject.MySqlData;
import com.gproject.adapter.OrderAdapter;
import com.gproject.androidcoffee12.MainActivity;
import com.gproject.androidcoffee12.R;
import com.gproject.entity.OrderEntity;
import com.gproject.entity.ProductListEntity;
import okhttp3.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {




    String URL;
    private List<OrderEntity> orderList = new ArrayList();
    ProgressBar progressBar;
    Button btnRefresh;
    Context context;
    String uphone;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    LinearLayout lyRefresh;
    MySqlData mySqlData;
    public OrderFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       URL=  ((MySqlData)getContext().getApplicationContext()).getURL()+"getaorder/";

        View view = inflater.inflate(R.layout.fragment_order,container,false);
        recyclerView = view.findViewById(R.id.order_view);
       layoutManager= new LinearLayoutManager(this.getActivity());

        progressBar = view.findViewById(R.id.progressBar);
        btnRefresh=view.findViewById(R.id.btn_refresh);
        lyRefresh=view.findViewById(R.id.ly_refresh);


        mySqlData = (MySqlData)getActivity().getApplication();
        uphone = mySqlData.getCustomerEntity().getPhone();
        context = this.getContext();
        requestOrder();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyRefresh.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                requestOrder();
                ((MySqlData)getActivity().getApplicationContext()).readCustomer();
            }
        });
        return view;

    }

    public void requestOrder()
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(URL+uphone)
                .build();
        Call call = client.newCall(request);


        //异步调用,并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        lyRefresh.setVisibility(View.VISIBLE);
                    }
                });
                Log.e("yzOrder", "yzOrderFail");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String str = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson =new Gson();
                        orderList =  gson.fromJson(str, new TypeToken<List<OrderEntity>>() {}.getType());
                        OrderAdapter adapter = new OrderAdapter(orderList,context);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                        lyRefresh.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        uphone = mySqlData.getCustomerEntity().getPhone();
        requestOrder();
    }
}

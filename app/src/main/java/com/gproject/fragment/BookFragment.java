package com.gproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.gproject.MySqlData;
import com.gproject.androidcoffee12.BookActivity;
import com.gproject.androidcoffee12.MainActivity;
import com.gproject.androidcoffee12.R;
import okhttp3.*;

import java.io.IOException;


public class BookFragment extends Fragment {
    private ImageButton db;
    private ImageButton th;
    TextView tv_shopStatus;
    String URL;
     boolean isWebSocket = true;


    public BookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_book,container,false);

        db = view.findViewById(R.id.btn_db);
        th = view.findViewById(R.id.btn_th);
        tv_shopStatus=view.findViewById(R.id.tv_shop_status);
        URL = ((MySqlData)getActivity().getApplication()).getURL();


        final Intent bookIntent = new Intent(getActivity(), BookActivity.class);
        th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookIntent.putExtra("db",false);
                startActivity(bookIntent);
            }
        });


        db.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               bookIntent.putExtra("db",true);
               startActivity(bookIntent);
           }
       });


       return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        checkStatus();
    }

    public void checkStatus()
    {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(URL + "android_get_shop_status")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getContext(),"客人，您的网络好像出了点问题哟",Toast.LENGTH_SHORT).show();
                isWebSocket =false;
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(str.equals("0"))
                        {
                            tv_shopStatus.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            tv_shopStatus.setVisibility(View.GONE);
                        }
                        if(!isWebSocket)
                        {
                            ((MySqlData)getContext().getApplicationContext()).readCustomer();
                            isWebSocket=true;
                        }
                    }
                });

            }
        });
    }
}
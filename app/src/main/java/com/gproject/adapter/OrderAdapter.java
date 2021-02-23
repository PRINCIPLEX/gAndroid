package com.gproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gproject.androidcoffee12.BookActivity;
import com.gproject.androidcoffee12.OrderDetailActivity;
import com.gproject.androidcoffee12.R;
import com.gproject.entity.OrderEntity;


import java.io.Serializable;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderEntity> mOrderList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{


        RecyclerView recViewProduct;
        TextView totalPrice;
        LinearLayout lyViewContent ;
        Button btn_book_again;



        public ViewHolder (View view)
        {
            super(view);

            recViewProduct =(RecyclerView) view.findViewById(R.id.recview_produtct);
            totalPrice = (TextView) view.findViewById(R.id.txt_total_price);
            btn_book_again = (Button)view.findViewById(R.id.btn_submit_order);
            lyViewContent =(LinearLayout)view.findViewById(R.id.view_content);

        }

    }

    public OrderAdapter(List <OrderEntity> orderEntity, Context context){
        mOrderList = orderEntity;
        mContext = context;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_order_list,parent,false);
        OrderAdapter.ViewHolder holder = new OrderAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position){

        OrderEntity orderEntity = mOrderList.get(position);
        holder.totalPrice.setText("实付： ￥"+String.valueOf(orderEntity.getTotalprice()));
        holder.btn_book_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent bookIntent = new Intent(mContext,BookActivity.class).putExtra("bookAgain", (Serializable) orderEntity.getProductEntities());
            bookIntent.putExtra("db",orderEntity.isIseatway());
            mContext.startActivity(bookIntent);

            }
        });


        LinearLayoutManager layoutManager= new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false);
        holder.recViewProduct.setLayoutManager(layoutManager);

        //嵌套recyceview
        OrderProductAdapter adapter = new OrderProductAdapter(orderEntity,mContext);
        holder.recViewProduct.setAdapter(adapter);

        //订单详情
        holder.lyViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderListIntent = new Intent(mContext, OrderDetailActivity.class).putExtra("orderList", (Serializable) orderEntity);
                mContext.startActivity(orderListIntent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return mOrderList.size();
    }


}

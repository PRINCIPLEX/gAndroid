package com.gproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.gproject.androidcoffee12.OrderDetailActivity;
import com.gproject.androidcoffee12.R;
import com.gproject.entity.OrderEntity;
import com.gproject.entity.ProductListEntity;

import java.io.Serializable;
import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder> {
    private List<ProductListEntity.ProductEntity> mCoffeeList;
    private Context mContext;
    private OrderEntity orderList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView productName;


        public ViewHolder(View view) {
            super(view);
            ivProduct = (ImageView) view.findViewById(R.id.iv_product);
            productName = (TextView) view.findViewById(R.id.tv_item_life_product_name);
        }

    }


    @Override
    public OrderProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_product, parent, false);
        OrderProductAdapter.ViewHolder holder = new OrderProductAdapter.ViewHolder(view);
        return holder;
    }

    public OrderProductAdapter(OrderEntity orderList, Context context) {
        this.orderList = orderList;
        mContext = context;
        mCoffeeList = orderList.getProductEntities();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductListEntity.ProductEntity coffee = mCoffeeList.get(position);
        holder.productName.setText(coffee.getProductName());
        byte[] imageBytes = coffee.getProductImg();
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.ivProduct.setImageBitmap(imageBitmap);


        //订单详情
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderListIntent = new Intent(mContext, OrderDetailActivity.class).putExtra("orderList", (Serializable) orderList);
                mContext.startActivity(orderListIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCoffeeList.size();
    }
}

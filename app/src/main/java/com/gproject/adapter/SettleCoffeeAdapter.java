package com.gproject.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.gproject.androidcoffee12.R;
import com.gproject.entity.ProductListEntity;

import java.util.List;

public class SettleCoffeeAdapter extends RecyclerView.Adapter<SettleCoffeeAdapter.ViewHolder> {
    private List<ProductListEntity.ProductEntity> mCoffeeList;



    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView coffeeImage;
        TextView coffeeName;
        TextView coffeeNum;
        TextView coffeePrice;

        public ViewHolder (View view)
        {
            super(view);
            coffeeImage=(ImageView)view.findViewById(R.id.iv_good);
            coffeeName = (TextView) view.findViewById(R.id.txt_shopname);
            coffeeNum =(TextView) view.findViewById(R.id.txt_quantity);
            coffeePrice = (TextView) view.findViewById(R.id.txt_price);
        }

    }

    public  SettleCoffeeAdapter (List <ProductListEntity.ProductEntity> coffeeList){
        mCoffeeList = coffeeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_shopping_product_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

            ProductListEntity.ProductEntity coffee = mCoffeeList.get(position);


            byte[] imageBytes = coffee.getProductImg();
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.coffeeImage.setImageBitmap(imageBitmap);
            holder.coffeeName.setText(coffee.getProductName());
            holder.coffeeNum.setText(String.valueOf(coffee.getProductCount()));
            holder.coffeePrice.setText(String.valueOf(coffee.getProductMoney()));

    }

    @Override
    public int getItemCount(){
        return mCoffeeList.size();
    }


}

package com.gproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gproject.androidcoffee12.R;
import com.gproject.entity.ProductListEntity;
import com.gproject.entity.ShopCart;
import com.gproject.impl.ShopCartImp;
import java.util.ArrayList;


public class ShoppingCartAdapter extends BaseAdapter {
    private Context context;
    private ShopCart shopCart;
    private ArrayList<ProductListEntity.ProductEntity> dishList;
    private ShopCartImp shopCartImp;
    private int itemCount;

    public ShoppingCartAdapter(Context context, ShopCart shopCart) {
        this.context = context;
        this.shopCart = shopCart;
        this.itemCount = shopCart.getShoppingAccount();
        this.dishList = new ArrayList<>();
        dishList.addAll(shopCart.getShoppingSingle().keySet());
    }

    @Override
    public int getCount() {
        return this.itemCount;
    }

    @Override
    public Object getItem(int position) {
        return "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart, parent, false);

            holder.iv_item_life_product =convertView.findViewById(R.id.iv_item_life_product);
            holder.tv_item_life_product_name = convertView.findViewById(R.id.tv_item_life_product_name);
            holder.tv_item_life_product_money = convertView.findViewById(R.id.tv_item_life_product_money);
            holder.iv_group_list_item_count_reduce = convertView.findViewById(R.id.iv_group_list_item_count_reduce);
            holder.tv_group_list_item_count_num = convertView.findViewById(R.id.tv_group_list_item_count_num);
            holder.iv_group_list_item_count_add = convertView.findViewById(R.id.iv_group_list_item_count_add);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ProductListEntity.ProductEntity dish = getDishByPosition(position);
        if (dish != null) {
            //信息绑定
            byte[] imageBytes = dish.getProductImg();
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.iv_item_life_product.setImageBitmap(imageBitmap);
            holder.tv_item_life_product_name.setText(dish.getProductName());
            holder.tv_item_life_product_money.setText("" + dish.getProductMoney());
            holder.tv_group_list_item_count_num.setText(dish.getProductCount() + "");

            holder.iv_group_list_item_count_reduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ShoppingCartAdapter.this.onUpdateNum(v, dish, false);

                    if (shopCart.subShoppingSingle(dish)) {
                        dishList.clear();
                        dishList.addAll(shopCart.getShoppingSingle().keySet());
                        itemCount = shopCart.getShoppingAccount();
                        notifyDataSetChanged();
                        if (shopCartImp != null)
                            shopCartImp.remove(v, position, dish);
//                        onItemChrldListner.onCall(v, dish, "reduce");
                    }
                }
            });
            holder.iv_group_list_item_count_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shopCart.addShoppingSingle(dish)) {
//                        notifyItemChanged(position);
                        notifyDataSetChanged();
                        if (shopCartImp != null)
                            shopCartImp.add(v, position, dish);
//                        onItemChrldListner.onCall(v, dish, "add");
                    }

                }
            });
        }


        return convertView;

    }


    public ProductListEntity.ProductEntity getDishByPosition(int position) {
        return dishList.get(position);
    }


    private class ViewHolder {
        private ImageView iv_item_life_product;
        private TextView tv_item_life_product_name;
        private TextView tv_item_life_product_money;

        private ImageView iv_group_list_item_count_reduce;
        private TextView tv_group_list_item_count_num;
        private ImageView iv_group_list_item_count_add;
    }


    //要定义一个按钮监听抽象接口和时间
    public interface OnItemChrldListner {
        void onCall(View view, ProductListEntity.ProductEntity entity, String type);
    }

    //定义一个监听 再activity中调用
    private OnItemChrldListner onItemChrldListner;

    public void setOnItemChrldListner(OnItemChrldListner onItemChrldListner) {
        this.onItemChrldListner = onItemChrldListner;
    }

    public ShopCartImp getShopCartImp() {
        return shopCartImp;
    }

    public void setShopCartImp(ShopCartImp shopCartImp) {
        this.shopCartImp = shopCartImp;
    }


    /**
     * 控制加减方法
     *
     * @param flag 标识加还是减
     */
    void onUpdateNum(View view, ProductListEntity.ProductEntity entity, boolean flag) {
        int initCount = entity.getProductCount();
        if (flag) {//加
            //设置num+1
            onItemChrldListner.onCall(view, entity, "add");
        } else {//减
            //判断是不是0，如果是减到0了，就通知不能在减了
            if (initCount - 1 > 0) {
                onItemChrldListner.onCall(view, entity, "reduce");
            } else {
                //不能在减了,如果当前为0了，则在activity中的购物车删除这条为0的数据
                initCount = 0;
                onItemChrldListner.onCall(view, entity, "zero");
            }
        }
    }

}

package com.gproject.impl;

import android.view.View;
import com.gproject.entity.ProductListEntity;


public interface ShopCartImp {
    void add(View view, int postion, ProductListEntity.ProductEntity entity);
    void remove(View view, int postion, ProductListEntity.ProductEntity entity);
}

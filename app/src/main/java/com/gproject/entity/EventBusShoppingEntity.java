package com.gproject.entity;


public class EventBusShoppingEntity {
    private ProductListEntity.ProductEntity entity;
    private String key;

    public ProductListEntity.ProductEntity getEntity() {
        return entity;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setEntity(ProductListEntity.ProductEntity entity) {
        this.entity = entity;
    }

    public EventBusShoppingEntity(ProductListEntity.ProductEntity entity, String showTotalPrice) {
        this.entity = entity;
        this.key = showTotalPrice;
    }
}

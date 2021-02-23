package com.gproject.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 商品列表2级
 */


public class ProductListEntity {



    private Long typeId;
    private String typeName;
    private List<ProductEntity> productEntities;
    private int typeCount;




    public static class ProductEntity implements Serializable {
        private Long productId;
        private String productName;
        private byte[] productImg;
        private Long productMonth;
        private Double productMoney;
        private int productCount;
        private Long parentId;//父ID，用来更新左侧列表使用

//////////////////////////test
        public ProductEntity(String productName, Long productMonth, Double productMoney, int productCount, Long productId, Long parentId) {
            this.productImg = productImg;
            this.productName = productName;
            this.productMonth = productMonth;
            this.productMoney = productMoney;
            this.productCount = productCount;
            this.productId = productId;
            this.parentId = parentId;
        }


        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public byte[] getProductImg() {
            return productImg;
        }

        public void setProductImg(byte[] productImg) {
            this.productImg = productImg;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Long getProductMonth() {
            return productMonth;
        }

        public void setProductMonth(Long productMonth) {
            this.productMonth = productMonth;
        }

        public Double getProductMoney() {
            return productMoney;
        }

        public void setProductMoney(Double productMoney) {
            this.productMoney = productMoney;
        }

        public int getProductCount() {
            return productCount;
        }

        public void setProductCount(int productCount) {
            this.productCount = productCount;
        }

        public Long getParentId() {
            return parentId;
        }

        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }
    }


    public int getTypeCount() {
        return typeCount;
    }

    public void setTypeCount(int typeCount) {
        this.typeCount = typeCount;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    public ProductListEntity(Long typeId, String typeName, List<ProductEntity> productEntities) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.productEntities = productEntities;
        this.typeCount = 0;
    }
}

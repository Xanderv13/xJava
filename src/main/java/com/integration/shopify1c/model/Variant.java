package com.integration.shopify1c.model;

import java.math.BigDecimal;

/**
 * ?????? ???????? ??????
 */
public class Variant {
    private String id;
    private String title;
    private BigDecimal price;
    private String sku;
    private Integer inventoryQuantity;
    private BigDecimal weight;
    private String weightUnit;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
    }
    
    public Integer getInventoryQuantity() {
        return inventoryQuantity;
    }
    
    public void setInventoryQuantity(Integer inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }
    
    public BigDecimal getWeight() {
        return weight;
    }
    
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
    
    public String getWeightUnit() {
        return weightUnit;
    }
    
    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }
}

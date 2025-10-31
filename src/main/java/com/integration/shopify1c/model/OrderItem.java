package com.integration.shopify1c.model;

import java.math.BigDecimal;

/**
 * ??????? ??????
 */
public class OrderItem {
    private String id;
    private String productId;
    private String variantId;
    private String title;
    private Integer quantity;
    private BigDecimal price;
    private String sku;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getVariantId() {
        return variantId;
    }
    
    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
}

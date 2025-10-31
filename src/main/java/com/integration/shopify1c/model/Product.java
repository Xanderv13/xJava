package com.integration.shopify1c.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * ?????? ?????? ??? ????????????? ????? Shopify ? 1?
 */
public class Product {
    private String id;
    private String title;
    private String description;
    private String vendor;
    private String productType;
    private BigDecimal price;
    private String sku;
    private Integer quantity;
    private List<String> images;
    private List<Variant> variants;
    
    // ??????? ? ???????
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getVendor() {
        return vendor;
    }
    
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
    public String getProductType() {
        return productType;
    }
    
    public void setProductType(String productType) {
        this.productType = productType;
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
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public List<String> getImages() {
        return images;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }
    
    public List<Variant> getVariants() {
        return variants;
    }
    
    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }
}

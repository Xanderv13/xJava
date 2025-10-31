package com.integration.shopify1c.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * ?????? ?????? ??? ????????????? ????? Shopify ? 1?
 */
public class Order {
    private String id;
    private String orderNumber;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    private String financialStatus;
    private BigDecimal totalPrice;
    private String currency;
    private Customer customer;
    private List<OrderItem> lineItems;
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getFinancialStatus() {
        return financialStatus;
    }
    
    public void setFinancialStatus(String financialStatus) {
        this.financialStatus = financialStatus;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public List<OrderItem> getLineItems() {
        return lineItems;
    }
    
    public void setLineItems(List<OrderItem> lineItems) {
        this.lineItems = lineItems;
    }
    
    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public BillingAddress getBillingAddress() {
        return billingAddress;
    }
    
    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }
}

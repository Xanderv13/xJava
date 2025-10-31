package com.integration.shopify1c.client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.integration.shopify1c.config.Config;
import com.integration.shopify1c.model.Order;
import com.integration.shopify1c.model.Product;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ?????? ??? ?????? ? Shopify API
 */
public class ShopifyClient {
    private static final Logger logger = LoggerFactory.getLogger(ShopifyClient.class);
    private static final String API_VERSION = "2024-01";
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String baseUrl;
    private final String accessToken;
    
    public ShopifyClient() {
        this.accessToken = Config.getShopifyAccessToken();
        String shopName = Config.getShopifyShopName();
        this.baseUrl = String.format("https://%s.myshopify.com/admin/api/%s", shopName, API_VERSION);
        
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
        
        this.gson = new Gson();
    }
    
    /**
     * ???????? ?????? ??????? ?? Shopify
     */
    public List<Product> getProducts() throws IOException {
        List<Product> products = new ArrayList<>();
        String url = baseUrl + "/products.json?limit=250";
        String nextPageUrl = url;
        
        while (nextPageUrl != null) {
            Request request = new Request.Builder()
                .url(nextPageUrl)
                .addHeader("X-Shopify-Access-Token", accessToken)
                .get()
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("?????? ??? ????????? ???????: " + response.code());
                }
                
                String responseBody = response.body().string();
                JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                JsonArray productsArray = jsonResponse.getAsJsonArray("products");
                
                for (JsonElement element : productsArray) {
                    Product product = parseProduct(element.getAsJsonObject());
                    products.add(product);
                }
                
                // ???????? ??????? ????????? ????????
                String linkHeader = response.header("Link");
                nextPageUrl = extractNextPageUrl(linkHeader);
            }
        }
        
        logger.info("???????? ??????? ?? Shopify: {}", products.size());
        return products;
    }
    
    /**
     * ???????? ?????? ??????? ?? Shopify
     */
    public List<Order> getOrders(Date sinceDate) throws IOException {
        List<Order> orders = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String sinceParam = sinceDate != null ? "&created_at_min=" + sdf.format(sinceDate) : "";
        String url = baseUrl + "/orders.json?limit=250&status=any" + sinceParam;
        String nextPageUrl = url;
        
        while (nextPageUrl != null) {
            Request request = new Request.Builder()
                .url(nextPageUrl)
                .addHeader("X-Shopify-Access-Token", accessToken)
                .get()
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("?????? ??? ????????? ???????: " + response.code());
                }
                
                String responseBody = response.body().string();
                JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                JsonArray ordersArray = jsonResponse.getAsJsonArray("orders");
                
                for (JsonElement element : ordersArray) {
                    Order order = parseOrder(element.getAsJsonObject());
                    orders.add(order);
                }
                
                String linkHeader = response.header("Link");
                nextPageUrl = extractNextPageUrl(linkHeader);
            }
        }
        
        logger.info("???????? ??????? ?? Shopify: {}", orders.size());
        return orders;
    }
    
    private Product parseProduct(JsonObject json) {
        Product product = new Product();
        product.setId(json.get("id").getAsString());
        product.setTitle(json.get("title").getAsString());
        
        if (json.has("body_html") && !json.get("body_html").isJsonNull()) {
            product.setDescription(json.get("body_html").getAsString());
        }
        
        if (json.has("vendor") && !json.get("vendor").isJsonNull()) {
            product.setVendor(json.get("vendor").getAsString());
        }
        
        if (json.has("product_type") && !json.get("product_type").isJsonNull()) {
            product.setProductType(json.get("product_type").getAsString());
        }
        
        // ??????? ?????????
        if (json.has("variants")) {
            List<com.integration.shopify1c.model.Variant> variants = new ArrayList<>();
            JsonArray variantsArray = json.getAsJsonArray("variants");
            for (JsonElement variantElement : variantsArray) {
                JsonObject variantJson = variantElement.getAsJsonObject();
                com.integration.shopify1c.model.Variant variant = new com.integration.shopify1c.model.Variant();
                variant.setId(variantJson.get("id").getAsString());
                variant.setTitle(variantJson.has("title") ? variantJson.get("title").getAsString() : "");
                variant.setPrice(new BigDecimal(variantJson.get("price").getAsString()));
                if (variantJson.has("sku") && !variantJson.get("sku").isJsonNull()) {
                    variant.setSku(variantJson.get("sku").getAsString());
                }
                if (variantJson.has("inventory_quantity") && !variantJson.get("inventory_quantity").isJsonNull()) {
                    variant.setInventoryQuantity(variantJson.get("inventory_quantity").getAsInt());
                }
                variants.add(variant);
            }
            product.setVariants(variants);
        }
        
        return product;
    }
    
    private Order parseOrder(JsonObject json) {
        Order order = new Order();
        order.setId(json.get("id").getAsString());
        order.setOrderNumber(json.get("order_number").getAsString());
        
        if (json.has("financial_status") && !json.get("financial_status").isJsonNull()) {
            order.setFinancialStatus(json.get("financial_status").getAsString());
        }
        
        if (json.has("fulfillment_status") && !json.get("fulfillment_status").isJsonNull()) {
            order.setStatus(json.get("fulfillment_status").getAsString());
        }
        
        if (json.has("total_price") && !json.get("total_price").isJsonNull()) {
            order.setTotalPrice(new BigDecimal(json.get("total_price").getAsString()));
        }
        
        if (json.has("currency") && !json.get("currency").isJsonNull()) {
            order.setCurrency(json.get("currency").getAsString());
        }
        
        // ??????? ???
        try {
            if (json.has("created_at") && !json.get("created_at").isJsonNull()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                order.setCreatedAt(sdf.parse(json.get("created_at").getAsString()));
            }
            if (json.has("updated_at") && !json.get("updated_at").isJsonNull()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                order.setUpdatedAt(sdf.parse(json.get("updated_at").getAsString()));
            }
        } catch (Exception e) {
            logger.warn("?????? ??? ???????? ??? ??????", e);
        }
        
        // ??????? ???????
        if (json.has("customer") && !json.get("customer").isJsonNull()) {
            JsonObject customerJson = json.getAsJsonObject("customer");
            com.integration.shopify1c.model.Customer customer = new com.integration.shopify1c.model.Customer();
            customer.setId(customerJson.get("id").getAsString());
            if (customerJson.has("email") && !customerJson.get("email").isJsonNull()) {
                customer.setEmail(customerJson.get("email").getAsString());
            }
            if (customerJson.has("first_name") && !customerJson.get("first_name").isJsonNull()) {
                customer.setFirstName(customerJson.get("first_name").getAsString());
            }
            if (customerJson.has("last_name") && !customerJson.get("last_name").isJsonNull()) {
                customer.setLastName(customerJson.get("last_name").getAsString());
            }
            if (customerJson.has("phone") && !customerJson.get("phone").isJsonNull()) {
                customer.setPhone(customerJson.get("phone").getAsString());
            }
            order.setCustomer(customer);
        }
        
        // ??????? ????????? ??????
        if (json.has("line_items")) {
            List<com.integration.shopify1c.model.OrderItem> lineItems = new ArrayList<>();
            JsonArray lineItemsArray = json.getAsJsonArray("line_items");
            for (JsonElement itemElement : lineItemsArray) {
                JsonObject itemJson = itemElement.getAsJsonObject();
                com.integration.shopify1c.model.OrderItem item = new com.integration.shopify1c.model.OrderItem();
                item.setId(itemJson.get("id").getAsString());
                if (itemJson.has("product_id") && !itemJson.get("product_id").isJsonNull()) {
                    item.setProductId(itemJson.get("product_id").getAsString());
                }
                if (itemJson.has("variant_id") && !itemJson.get("variant_id").isJsonNull()) {
                    item.setVariantId(itemJson.get("variant_id").getAsString());
                }
                if (itemJson.has("title") && !itemJson.get("title").isJsonNull()) {
                    item.setTitle(itemJson.get("title").getAsString());
                }
                if (itemJson.has("quantity") && !itemJson.get("quantity").isJsonNull()) {
                    item.setQuantity(itemJson.get("quantity").getAsInt());
                }
                if (itemJson.has("price") && !itemJson.get("price").isJsonNull()) {
                    item.setPrice(new BigDecimal(itemJson.get("price").getAsString()));
                }
                if (itemJson.has("sku") && !itemJson.get("sku").isJsonNull()) {
                    item.setSku(itemJson.get("sku").getAsString());
                }
                lineItems.add(item);
            }
            order.setLineItems(lineItems);
        }
        
        // ??????? ?????? ????????
        if (json.has("shipping_address") && !json.get("shipping_address").isJsonNull()) {
            JsonObject addressJson = json.getAsJsonObject("shipping_address");
            com.integration.shopify1c.model.ShippingAddress address = new com.integration.shopify1c.model.ShippingAddress();
            if (addressJson.has("address1")) address.setAddress1(addressJson.get("address1").getAsString());
            if (addressJson.has("address2")) address.setAddress2(addressJson.get("address2").getAsString());
            if (addressJson.has("city")) address.setCity(addressJson.get("city").getAsString());
            if (addressJson.has("province")) address.setProvince(addressJson.get("province").getAsString());
            if (addressJson.has("zip")) address.setZip(addressJson.get("zip").getAsString());
            if (addressJson.has("country")) address.setCountry(addressJson.get("country").getAsString());
            if (addressJson.has("first_name")) address.setFirstName(addressJson.get("first_name").getAsString());
            if (addressJson.has("last_name")) address.setLastName(addressJson.get("last_name").getAsString());
            order.setShippingAddress(address);
        }
        
        // ??????? ?????? ??? ??????????? ?????
        if (json.has("billing_address") && !json.get("billing_address").isJsonNull()) {
            JsonObject addressJson = json.getAsJsonObject("billing_address");
            com.integration.shopify1c.model.BillingAddress address = new com.integration.shopify1c.model.BillingAddress();
            if (addressJson.has("address1")) address.setAddress1(addressJson.get("address1").getAsString());
            if (addressJson.has("address2")) address.setAddress2(addressJson.get("address2").getAsString());
            if (addressJson.has("city")) address.setCity(addressJson.get("city").getAsString());
            if (addressJson.has("province")) address.setProvince(addressJson.get("province").getAsString());
            if (addressJson.has("zip")) address.setZip(addressJson.get("zip").getAsString());
            if (addressJson.has("country")) address.setCountry(addressJson.get("country").getAsString());
            if (addressJson.has("first_name")) address.setFirstName(addressJson.get("first_name").getAsString());
            if (addressJson.has("last_name")) address.setLastName(addressJson.get("last_name").getAsString());
            order.setBillingAddress(address);
        }
        
        return order;
    }
    
    private String extractNextPageUrl(String linkHeader) {
        if (linkHeader == null) {
            return null;
        }
        
        // ??????? ????????? Link ??? ????????? ????????? ????????
        // ??????: <url>; rel="next"
        String[] links = linkHeader.split(",");
        for (String link : links) {
            if (link.contains("rel=\"next\"")) {
                int start = link.indexOf('<');
                int end = link.indexOf('>');
                if (start != -1 && end != -1) {
                    return link.substring(start + 1, end);
                }
            }
        }
        return null;
    }
}

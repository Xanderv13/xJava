package com.integration.shopify1c.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.integration.shopify1c.config.Config;
import com.integration.shopify1c.model.Order;
import com.integration.shopify1c.model.Product;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * ?????? ??? ?????? ? 1? ????? REST API
 */
public class OneCClient {
    private static final Logger logger = LoggerFactory.getLogger(OneCClient.class);
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String baseUrl;
    private final String credentials;
    
    public OneCClient() {
        this.baseUrl = Config.getOneCBaseUrl();
        String username = Config.getOneCUsername();
        String password = Config.getOneCPassword();
        
        // ??????? HTTP ??????????????
        String auth = username + ":" + password;
        this.credentials = Base64.getEncoder().encodeToString(auth.getBytes());
        
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
        
        this.gson = new Gson();
    }
    
    /**
     * ????????? ????? ? 1?
     */
    public boolean sendProduct(Product product) throws IOException {
        JsonObject jsonProduct = convertProductTo1CFormat(product);
        
        RequestBody body = RequestBody.create(
            jsonProduct.toString(),
            MediaType.parse("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
            .url(baseUrl + "/products")
            .addHeader("Authorization", "Basic " + credentials)
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("?????? ??? ???????? ?????? ? 1?: {} - {}", response.code(), response.body().string());
                return false;
            }
            logger.info("????? ??????? ????????? ? 1?: {}", product.getSku());
            return true;
        }
    }
    
    /**
     * ????????? ????? ? 1?
     */
    public boolean sendOrder(Order order) throws IOException {
        JsonObject jsonOrder = convertOrderTo1CFormat(order);
        
        RequestBody body = RequestBody.create(
            jsonOrder.toString(),
            MediaType.parse("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
            .url(baseUrl + "/orders")
            .addHeader("Authorization", "Basic " + credentials)
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("?????? ??? ???????? ?????? ? 1?: {} - {}", response.code(), response.body().string());
                return false;
            }
            logger.info("????? ??????? ????????? ? 1?: {}", order.getOrderNumber());
            return true;
        }
    }
    
    /**
     * ???????? ?????? ?? 1?
     */
    public java.util.List<Product> getProducts() throws IOException {
        Request request = new Request.Builder()
            .url(baseUrl + "/products")
            .addHeader("Authorization", "Basic " + credentials)
            .get()
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("?????? ??? ????????? ??????? ?? 1?: " + response.code());
            }
            
            String responseBody = response.body().string();
            // ??????? ?????? ?? 1? ? ?????????????? ? ?????? ???????
            // ????? ????? ???????????? ??? ?????????? ?????? API 1?
            
            logger.info("???????? ?????? ?? 1?");
            return new java.util.ArrayList<>();
        }
    }
    
    /**
     * ?????????????? ?????? ? ?????? 1?
     */
    private JsonObject convertProductTo1CFormat(Product product) {
        JsonObject json = new JsonObject();
        
        if (product.getSku() != null) {
            json.addProperty("???????", product.getSku());
        }
        json.addProperty("????????????", product.getTitle());
        
        if (product.getDescription() != null) {
            json.addProperty("????????", product.getDescription());
        }
        
        if (product.getVariants() != null && !product.getVariants().isEmpty()) {
            var variant = product.getVariants().get(0);
            if (variant.getPrice() != null) {
                json.addProperty("????", variant.getPrice().toString());
            }
            if (variant.getInventoryQuantity() != null) {
                json.addProperty("???????", variant.getInventoryQuantity());
            }
        }
        
        if (product.getVendor() != null) {
            json.addProperty("?????????????", product.getVendor());
        }
        
        return json;
    }
    
    /**
     * ?????????????? ?????? ? ?????? 1?
     */
    private JsonObject convertOrderTo1CFormat(Order order) {
        JsonObject json = new JsonObject();
        
        json.addProperty("?????", order.getOrderNumber());
        json.addProperty("????", order.getCreatedAt() != null ? 
            new java.text.SimpleDateFormat("yyyy-MM-dd").format(order.getCreatedAt()) : "");
        json.addProperty("?????", order.getTotalPrice() != null ? order.getTotalPrice().toString() : "0");
        
        if (order.getCustomer() != null) {
            JsonObject customerJson = new JsonObject();
            customerJson.addProperty("???", order.getCustomer().getFirstName());
            customerJson.addProperty("???????", order.getCustomer().getLastName());
            customerJson.addProperty("Email", order.getCustomer().getEmail());
            customerJson.addProperty("???????", order.getCustomer().getPhone());
            json.add("??????", customerJson);
        }
        
        if (order.getLineItems() != null && !order.getLineItems().isEmpty()) {
            com.google.gson.JsonArray itemsArray = new com.google.gson.JsonArray();
            for (var item : order.getLineItems()) {
                JsonObject itemJson = new JsonObject();
                itemJson.addProperty("???????", item.getSku());
                itemJson.addProperty("????????????", item.getTitle());
                itemJson.addProperty("??????????", item.getQuantity());
                itemJson.addProperty("????", item.getPrice() != null ? item.getPrice().toString() : "0");
                itemsArray.add(itemJson);
            }
            json.add("??????", itemsArray);
        }
        
        return json;
    }
}

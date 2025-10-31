package com.integration.shopify1c.config;

import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ???????????? ??????????
 */
public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static com.typesafe.config.Config config;
    
    static {
        try {
            config = ConfigFactory.load("application.conf");
        } catch (Exception e) {
            logger.warn("?? ??????? ????????? application.conf, ???????????? ???????? ?? ?????????", e);
            config = ConfigFactory.empty();
        }
    }
    
    // Shopify ?????????
    public static String getShopifyApiKey() {
        return config.hasPath("shopify.apiKey") 
            ? config.getString("shopify.apiKey") 
            : System.getenv("SHOPIFY_API_KEY");
    }
    
    public static String getShopifyApiSecret() {
        return config.hasPath("shopify.apiSecret") 
            ? config.getString("shopify.apiSecret") 
            : System.getenv("SHOPIFY_API_SECRET");
    }
    
    public static String getShopifyShopName() {
        return config.hasPath("shopify.shopName") 
            ? config.getString("shopify.shopName") 
            : System.getenv("SHOPIFY_SHOP_NAME");
    }
    
    public static String getShopifyAccessToken() {
        return config.hasPath("shopify.accessToken") 
            ? config.getString("shopify.accessToken") 
            : System.getenv("SHOPIFY_ACCESS_TOKEN");
    }
    
    // 1C ?????????
    public static String getOneCBaseUrl() {
        return config.hasPath("onec.baseUrl") 
            ? config.getString("onec.baseUrl") 
            : System.getenv("ONEC_BASE_URL");
    }
    
    public static String getOneCUsername() {
        return config.hasPath("onec.username") 
            ? config.getString("onec.username") 
            : System.getenv("ONEC_USERNAME");
    }
    
    public static String getOneCPassword() {
        return config.hasPath("onec.password") 
            ? config.getString("onec.password") 
            : System.getenv("ONEC_PASSWORD");
    }
    
    // ????? ?????????
    public static int getSyncIntervalMinutes() {
        return config.hasPath("sync.intervalMinutes") 
            ? config.getInt("sync.intervalMinutes") 
            : 30;
    }
    
    public static boolean isSyncProductsEnabled() {
        return config.hasPath("sync.productsEnabled") 
            ? config.getBoolean("sync.productsEnabled") 
            : true;
    }
    
    public static boolean isSyncOrdersEnabled() {
        return config.hasPath("sync.ordersEnabled") 
            ? config.getBoolean("sync.ordersEnabled") 
            : true;
    }
}

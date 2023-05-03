package com.bookcan.deliver.productstore.model;
import com.google.gson.JsonObject;
public class MyOrderResponse {

    private String id;
    private String order_id;
    private String delivery_status;
    private String grand_total;
    private String updated_at;

    public String getId() {
        return id;
    }
    public String getOrder_id() {
        return order_id;
    }
    public String getDelivery_status() {
        return delivery_status;
    }
    public String getGrand_total() {
        return grand_total;
    }
    public String getUpdated_at() {
        return updated_at;
    }
}

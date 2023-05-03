package com.bookcan.deliver.productstore.model;
public class CheckoutResponse {
    private String message;
    private String status;
    private String order_id;
    private String customer_id;
    public String getMessage() {
        return message;
    }
    public String getStatus() {
        return status;
    }
    public String getOrder_id() {
        return order_id;
    }
    public String getCustomer_id() {
        return customer_id;
    }
}

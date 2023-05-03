package com.bookcan.deliver.productstore.model;

import java.util.List;
public class CartDashboard {
    private String grand_total;
    private String delivery_charge;
    private String lift_charge;
    private String total_pay;
    private String message;
    private List<CartProduct> cart_products = null;


    public List<CartProduct> getCoupons() {
        return cart_products;
    }
    public String getGrand_total() {
        return grand_total;
    }
    public String getDelivery_charge() {
        return delivery_charge;
    }
    public String getTotal_pay() {
        return total_pay;
    }
    public String getMessage() {
        return message;
    }
    public String getLift_charge() {
        return lift_charge;
    }
}


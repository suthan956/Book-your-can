package com.bookcan.deliver.productstore.model;
import com.google.gson.annotations.SerializedName;
public class Bookcancoupons {
    private String coupon_id;
    private String coupon_name;
    private String coupon_image;
    private String actual_price;
    private String final_price;
    private String coupon_purchased_id;
    private String total_cans;
    private String pending_available_cans;
    private String amount;

    public String getCoupon_id() {
        return coupon_id;
    }
    public String getCoupon_name() {
        return coupon_name;
    }
    public String getCoupon_image() {
        return coupon_image;
    }
    public String getActual_price() {
        return actual_price;
    }
    public String getFinal_price() {
        return final_price;
    }
    public String getCoupon_purchased_id() {
        return coupon_purchased_id;
    }
    public String getTotal_cans() {
        return total_cans;
    }
    public String getPending_available_cans() {
        return pending_available_cans;
    }
    public String getAmount() {
        return amount;
    }
}

package com.bookcan.deliver.productstore.model;

import java.util.ArrayList;
import java.util.List;

public class OrderList {
    private String emp_code;
    private String emp_name;
    private String order_id;
    private String slot_time;
    private String allocate_date;
    private String customer_id;
    private String grand_total;
    private String delivery_slot;
    private String created_at;
    private String mode_of_payment;
    private String customer_name;
    private String mobile;
    private String house_flat_no;
    private String street_name;
    private String address;
    private String landmark;
    private String pincode;
    private String lift_availability;
    private String total_floors;
    private String latitude;
    private String longtitude;
    private String distance;
    private List< OrderListEmployee > order_products;

    // Getter Methods

    public String getEmp_code() {
        return emp_code;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getSlot_time() {
        return slot_time;
    }

    public String getAllocate_date() {
        return allocate_date;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public String getDelivery_slot() {
        return delivery_slot;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getMode_of_payment() {
        return mode_of_payment;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getHouse_flat_no() {
        return house_flat_no;
    }

    public String getStreet_name() {
        return street_name;
    }

    public String getAddress() {
        return address;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public String getLift_availability() {
        return lift_availability;
    }

    public String getTotal_floors() {
        return total_floors;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public String getDistance() {
        return distance;
    }

    public List<OrderListEmployee> getOrder_products() {
        return order_products;
    }
}

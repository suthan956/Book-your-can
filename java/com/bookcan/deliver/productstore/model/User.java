package com.bookcan.deliver.productstore.model;

 
public class User {
    private String reg_id;
    private String customer_id;
    private String role_value;
    private String name;
    private String email;
    private String mobile;
    private String address;
    private String latitude;
    private String longtitude;
    private String house_flat_no;
    private String street_name;
    private String landmark;
    private String pincode;
    private String lift_availability;
    private String total_floors;
    private String updated_at;
    private String created_at;


    public User(String reg_id, String customer_id, String role_value, String name, String email, String mobile, String address, String latitude, String longtitude, String house_flat_no, String street_name, String landmark, String pincode, String lift_availability, String total_floors, String updated_at, String created_at) {
        this.reg_id = reg_id;
        this.customer_id = customer_id;
        this.role_value = role_value;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.house_flat_no = house_flat_no;
        this.street_name = street_name;
        this.landmark = landmark;
        this.pincode = pincode;
        this.lift_availability = lift_availability;
        this.total_floors = total_floors;
        this.updated_at = updated_at;
        this.created_at = created_at;
    }
    public String getReg_id() {
        return reg_id;
    }
    public String getCustomer_id() {
        return customer_id;
    }
    public String getRole_value() {
        return role_value;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getMobile() {
        return mobile;
    }
    public String getAddress() {
        return address;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongtitude() {
        return longtitude;
    }
    public String getHouse_flat_no() {
        return house_flat_no;
    }
    public String getStreet_name() {
        return street_name;
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
    public String getUpdated_at() {
        return updated_at;
    }
    public String getCreated_at() {
        return created_at;
    }
}

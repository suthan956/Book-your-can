package com.bookcan.deliver.productstore.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class DashboardCoupons {
    private String status;
    private String customer_id;
    private List<Bookcancoupons> coupons = null;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomer_id() {
        return customer_id;
    }
    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
    public List<Bookcancoupons> getCoupons() {
        return coupons;
    }
}


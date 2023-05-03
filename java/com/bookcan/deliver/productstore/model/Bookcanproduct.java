package com.bookcan.deliver.productstore.model;
import com.google.gson.annotations.SerializedName;
public class Bookcanproduct {
    @SerializedName("replacement_id")
    private String replacement_id;
    @SerializedName("can_id")
    private String can_id;
    @SerializedName("brand_type_id")
    private String brand_type_id;
    @SerializedName("brand_type")
    private String brand_type;
    @SerializedName("product_id")
    private String product_id;
    @SerializedName("product_code")
    private String product_code;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("img_thumbnail")
    private String img_thumbnail;
    @SerializedName("product_litre")
    private String product_litre;
    @SerializedName("actual_price")
    private String actual_price;
    @SerializedName("final_price")
    private String final_price;
    @SerializedName("is_replaceable")
    private String is_replaceable;
    @SerializedName("discount_percentage")
    private String discount_percentage;
    @SerializedName("product_des")
    private String product_des;
    @SerializedName("type")
    private String type;
    @SerializedName("coupon_id")
    private String coupon_id;
    @SerializedName("total_cans")
    private String total_cans;

    public Bookcanproduct(String replacement_id, String can_id, String brand_type_id, String brand_type, String product_id, String product_code, String product_name, String img_thumbnail, String product_litre, String actual_price, String final_price, String is_replaceable, String discount_percentage, String product_des, String type,String coupon_id,String total_cans) {
        this.replacement_id = replacement_id;
        this.can_id = can_id;
        this.brand_type_id = brand_type_id;
        this.brand_type = brand_type;
        this.product_id = product_id;
        this.product_code = product_code;
        this.product_name = product_name;
        this.img_thumbnail = img_thumbnail;
        this.product_litre = product_litre;
        this.actual_price = actual_price;
        this.final_price = final_price;
        this.is_replaceable = is_replaceable;
        this.discount_percentage = discount_percentage;
        this.product_des = product_des;
        this.type = type;
        this.coupon_id = coupon_id;
        this.total_cans =total_cans;
    }

    public String getReplacement_id() {
        return replacement_id;
    }
    public String getCan_id() {
        return can_id;
    }
    public String getBrand_type_id() {
        return brand_type_id;
    }
    public String getBrand_type() {
        return brand_type;
    }
    public String getProduct_id() {
        return product_id;
    }
    public String getProduct_code() {
        return product_code;
    }
    public String getProduct_name() {
        return product_name;
    }
    public String getImg_thumbnail() {
        return img_thumbnail;
    }
    public String getProduct_litre() {
        return product_litre;
    }
    public String getActual_price() {
        return actual_price;
    }
    public String getFinal_price() {
        return final_price;
    }
    public String getIs_replaceable() {
        return is_replaceable;
    }
    public String getDiscount_percentage() {
        return discount_percentage;
    }
    public String getType() {
        return type;
    }
    public String getProduct_des() {
        return product_des;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public String getTotal_cans() {
        return total_cans;
    }
}

package com.bookcan.deliver.productstore.model;
import com.google.gson.annotations.SerializedName;
public class CartProduct {
    @SerializedName("replacement_id")
    private String replacement_id;
    @SerializedName("can_id")
    private String can_id;
    @SerializedName("customer_id")
    private String customer_id;
    @SerializedName("brand_type")
    private String brand_type;
    @SerializedName("product_id")
    private String product_id;
    @SerializedName("product_code")
    private String product_code;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("image")
    private String image;
    @SerializedName("product_litre")
    private String product_litre;
    @SerializedName("actual_price")
    private String actual_price;
    @SerializedName("final_price")
    private String final_price;
    @SerializedName("can_price")
    private String can_price;
    @SerializedName("total_can_quantity")
    private String total_can_quantity;
    @SerializedName("total_can_price")
    private String total_can_price;
    @SerializedName("total_replacement_quantity")
    private String total_replacement_quantity;
    @SerializedName("total_replacement_price")
    private String total_replacement_price;
    @SerializedName("total_quantity")
    private String total_quantity;
    @SerializedName("total_price")
    private String total_price;

    public CartProduct(String replacement_id, String can_id, String customer_id, String brand_type, String product_id, String product_code, String product_name, String image, String product_litre, String actual_price, String final_price, String can_price, String total_can_quantity, String total_can_price, String total_replacement_quantity, String total_replacement_price, String total_quantity, String total_price) {
        this.replacement_id = replacement_id;
        this.can_id = can_id;
        this.customer_id = customer_id;
        this.brand_type = brand_type;
        this.product_id = product_id;
        this.product_code = product_code;
        this.product_name = product_name;
        this.image = image;
        this.product_litre = product_litre;
        this.actual_price = actual_price;
        this.final_price = final_price;
        this.can_price = can_price;
        this.total_can_quantity = total_can_quantity;
        this.total_can_price = total_can_price;
        this.total_replacement_quantity = total_replacement_quantity;
        this.total_replacement_price = total_replacement_price;
        this.total_quantity = total_quantity;
        this.total_price = total_price;
    }


    public String getReplacement_id() {
        return replacement_id;
    }
    public String getCan_id() {
        return can_id;
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
    public String getProduct_litre() {
        return product_litre;
    }
    public String getActual_price() {
        return actual_price;
    }
    public String getFinal_price() {
        return final_price;
    }

    public String getCustomer_id() {
        return customer_id;
    }
    public String getImage() {
        return image;
    }
    public String getCan_price() {
        return can_price;
    }
    public String getTotal_can_quantity() {
        return total_can_quantity;
    }
    public String getTotal_can_price() {
        return total_can_price;
    }
    public String getTotal_replacement_quantity() {
        return total_replacement_quantity;
    }
    public String getTotal_replacement_price() {
        return total_replacement_price;
    }
    public String getTotal_quantity() {
        return total_quantity;
    }
    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
    public String getTotal_price() {
        return total_price;
    }
}

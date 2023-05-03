package com.bookcan.deliver.productstore.model;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class ProductArr {
    @SerializedName("bookcanproducts")
    private List<Bookcanproduct> bookcanproducts;

    public List<Bookcanproduct> getBookcanproducts() {
        return bookcanproducts;
    }
}

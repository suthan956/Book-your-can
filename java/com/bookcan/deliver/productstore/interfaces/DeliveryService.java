package com.bookcan.deliver.productstore.interfaces;
import com.bookcan.deliver.productstore.model.CheckoutResponse;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
public interface DeliveryService {
    @POST("/byc_app_api_v1/delivery_time.php")
    Call<ArrayList<String>> deliveryCall(@Header("App-Authorization") String AppAuth,
                                 @Header("User-Authorization-1") String UserAuth1,
                                 @Header("User-Authorization-2") String UserAuth2);
}

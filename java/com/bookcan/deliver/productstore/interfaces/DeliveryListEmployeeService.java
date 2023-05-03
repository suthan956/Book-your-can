package com.bookcan.deliver.productstore.interfaces;

import com.bookcan.deliver.productstore.model.Bookcanproduct;
import com.bookcan.deliver.productstore.model.OrderList;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DeliveryListEmployeeService {

    @POST("/byc_app_api_v1/employee_order_list.php")
    Call<List<OrderList>> getDeliveryList(@Header("App-Authorization") String AppAuth,
                                          @Header("User-Authorization-1") String UserAuth1,
                                          @Header("User-Authorization-2") String UserAuth2,
                                          @Body RequestBody requestBody);
}

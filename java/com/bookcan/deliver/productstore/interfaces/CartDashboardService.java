package com.bookcan.deliver.productstore.interfaces;
import com.bookcan.deliver.productstore.model.AddToCartResponse;
import com.bookcan.deliver.productstore.model.CartDashboard;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
public interface CartDashboardService {
    @POST("/byc_app_api_v1/cart.php")
    Call<CartDashboard> addCartCall(@Header("App-Authorization") String AppAuth,
                                    @Header("User-Authorization-1") String UserAuth1,
                                    @Header("User-Authorization-2") String UserAuth2,
                                    @Body RequestBody requestBody);

}

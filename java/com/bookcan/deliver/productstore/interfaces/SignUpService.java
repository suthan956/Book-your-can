package com.bookcan.deliver.productstore.interfaces;
import com.bookcan.deliver.productstore.model.CheckoutResponse;
import com.bookcan.deliver.productstore.model.LoginResponse;
import com.bookcan.deliver.productstore.model.SignUpResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
public interface SignUpService {
    @POST("/byc_app_api_v1/verify_customer.php")
    Call<SignUpResponse> signUpCall(@Header("App-Authorization") String AppAuth,
                                    @Header("User-Authorization-1") String UserAuth1,
                                    @Header("User-Authorization-2") String UserAuth2,
                                    @Body RequestBody requestBody);

}

package com.bookcan.deliver.productstore.interfaces;
import com.bookcan.deliver.productstore.model.LoginResponse;
import com.bookcan.deliver.productstore.model.SignUpResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OtpService {
    @POST("/byc_app_api_v1/validate_otp_register.php")
    Call<SignUpResponse> userLoginOtp(@Header("App-Authorization") String AppAuth, @Body RequestBody requestBody);

}

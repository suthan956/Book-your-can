package com.bookcan.deliver.productstore.interfaces;
import com.bookcan.deliver.productstore.model.ForgetResponse;
import com.bookcan.deliver.productstore.model.LoginResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
public interface ForgetPasswordService {
    @POST("/byc_app_api_v1/forgetPwd.php")
    Call<ForgetResponse> forgetPasswordService(@Header("App-Authorization") String AppAuth, @Body RequestBody requestBody);

}

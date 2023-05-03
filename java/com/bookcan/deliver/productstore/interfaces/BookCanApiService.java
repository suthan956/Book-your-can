package com.bookcan.deliver.productstore.interfaces;
import com.bookcan.deliver.productstore.model.Bookcanproduct;
import com.bookcan.deliver.productstore.model.ProductArr;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface BookCanApiService {

    @GET("/byc_app_api_v1/products.php/")
    Call<List<Bookcanproduct>> getProduct(@Header("App-Authorization") String AppAuth,
                                          @Header("User-Authorization-1") String UserAuth1,
                                          @Header("User-Authorization-2") String UserAuth2);

}

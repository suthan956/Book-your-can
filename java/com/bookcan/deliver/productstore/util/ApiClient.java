package com.bookcan.deliver.productstore.util;
import com.bookcan.deliver.productstore.interfaces.AddCartService;
import com.bookcan.deliver.productstore.interfaces.CartDashboardService;
import com.bookcan.deliver.productstore.interfaces.CheckoutService;
import com.bookcan.deliver.productstore.interfaces.CouponApiService;
import com.bookcan.deliver.productstore.interfaces.CouponCheckoutService;
import com.bookcan.deliver.productstore.interfaces.DeliveryService;
import com.bookcan.deliver.productstore.interfaces.ForgetPasswordService;
import com.bookcan.deliver.productstore.interfaces.LoginService;
import com.bookcan.deliver.productstore.interfaces.MyOrderService;
import com.bookcan.deliver.productstore.interfaces.OtpService;
import com.bookcan.deliver.productstore.interfaces.PinCodeService;
import com.bookcan.deliver.productstore.interfaces.SignUpService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {

    private static Retrofit getRetrofit(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://bookyourcan.com/")
                .client(okHttpClient)
                .build();

        return retrofit;
    }


    public static LoginService getUserService(){
        LoginService userService = getRetrofit().create(LoginService.class);

        return userService;
    }

    public static OtpService getOtpService(){
        OtpService userService = getRetrofit().create(OtpService.class);

        return userService;
    }

    public static ForgetPasswordService getForgetService(){
        ForgetPasswordService forgetPasswordService = getRetrofit().create(ForgetPasswordService.class);

        return forgetPasswordService;
    }

    public static PinCodeService getPincodeService(){
        PinCodeService pinCodeService = getRetrofit().create(PinCodeService.class);

        return pinCodeService;
    }

    public static CouponApiService getCouponService(){
        CouponApiService couponApiService = getRetrofit().create(CouponApiService.class);

        return couponApiService;
    }

    public static AddCartService getAddCartService(){
        AddCartService addCartService = getRetrofit().create(AddCartService.class);

        return addCartService;
    }

    public static CartDashboardService getCartDashboardService(){
        CartDashboardService cartDashboardService = getRetrofit().create(CartDashboardService.class);

        return cartDashboardService;
    }

    public static MyOrderService getOrderService(){
        MyOrderService myOrderService = getRetrofit().create(MyOrderService.class);

        return myOrderService;
    }

    public static CheckoutService getCheckoutService(){
        CheckoutService checkoutService = getRetrofit().create(CheckoutService.class);

        return checkoutService;
    }

    public static DeliveryService getDeliveryService(){
        DeliveryService deliveryService = getRetrofit().create(DeliveryService.class);

        return deliveryService;
    }

    public static CouponCheckoutService getCouponCheckoutService(){
        CouponCheckoutService deliveryService = getRetrofit().create(CouponCheckoutService.class);

        return deliveryService;
    }

    public static SignUpService getSignUpService(){
        SignUpService deliveryService = getRetrofit().create(SignUpService.class);

        return deliveryService;
    }

}

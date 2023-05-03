package com.bookcan.deliver.productstore.util.localstorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bookcan.deliver.productstore.model.Bookcanproduct;
import com.bookcan.deliver.productstore.model.OrderList;

import java.util.List;


public class LocalStorage {


    public static final String KEY_HASH = "hash";
    public static final String RECIPE_SLIDER = "recipeSlider";
    public static final String KEY_USER = "User";
    public static final String KEY_USER_ADDRESS = "user_address";
    public static final String KEY_PREFERENCES = "preferences";
    public static final String USER_PREFERENCES = "user_preferences";
    public static final String USER_NAME = "user_name";
    public static final String USER_EMAIL = "user_email";
    public static final String SLIDER_IMAGE = "slider_image";
    public static final String ADVERTISE_IMAGE = "advertise_image";
    public static final String CATEGORY = "category";
    public static final String FAVORITE_CATEGORY = "fav_category";
    public static final String USER_AUTH1 = "User_Auth_1";
    public static final String USER_AUTH2 = "User_Auth_2";

    public static final String CUSTOMER_ID = "Customer_Id";
    public static final String PRODUCT_CODE = "Product_Code";

    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static List<Bookcanproduct> listOfProducts = null;
    public static List<OrderList> listOfDelivery = null;

    private static LocalStorage instance = null;
    SharedPreferences sharedPreferences;
    Editor editor;
    int PRIVATE_MODE = 0;
    Context _context;

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences("Preferences", 0);
    }

    public static LocalStorage getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalStorage.class) {
                if (instance == null) {
                    instance = new LocalStorage(context);
                }
            }
        }
        return instance;
    }

    public void createUserLoginSession(String user) {
        editor = sharedPreferences.edit();
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_USER, user);
        editor.commit();
    }

    public void setUserAuthorization(String auth1,String auth2) {
        editor = sharedPreferences.edit();
        editor.putString(USER_AUTH1, auth1);
        editor.putString(USER_AUTH2, auth2);
        editor.commit();
    }

    public void setProductCode(String id) {
        editor = sharedPreferences.edit();
        editor.putString(PRODUCT_CODE, id);
        editor.commit();
    }

    public String getProductCode(){
        return sharedPreferences.getString(PRODUCT_CODE,"");
    }

    public void setCustomerId(String id) {
        editor = sharedPreferences.edit();
        editor.putString(CUSTOMER_ID, id);
        editor.commit();
    }

    public String getCustomerId(){
        return sharedPreferences.getString(CUSTOMER_ID,"");
    }

    public String[] getUserAuthorization(){
        return new String[]{sharedPreferences.getString(USER_AUTH1,""),
                sharedPreferences.getString(USER_AUTH2, "")};
    }

    public String getUserLogin() {
        return sharedPreferences.getString(KEY_USER, "");
    }


    public void logoutUser() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public boolean checkLogin() {
        // Check login status
        return !this.isUserLoggedIn();
    }


    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, false);
    }

    public String getUserAddress() {
        if (sharedPreferences.contains(KEY_USER_ADDRESS))
            return sharedPreferences.getString(KEY_USER_ADDRESS, null);
        else return null;
    }


    public void setUserAddress(String user_address) {
        Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ADDRESS, user_address);
        editor.commit();
    }

    public String getCart() {
        if (sharedPreferences.contains("CART"))
            return sharedPreferences.getString("CART", null);
        else return null;
    }


    public void setCart(String cart) {
        Editor editor = sharedPreferences.edit();
        editor.putString("CART", cart);
        editor.commit();
    }

    public String getCustomerOrEmployee() {
        if (sharedPreferences.contains("ROLE"))
            return sharedPreferences.getString("ROLE", null);
        else return null;
    }

    public void setCustomerOrEmployee(String role) {
        Editor editor = sharedPreferences.edit();
        editor.putString("ROLE", role);
        editor.commit();
    }

    public void deleteCart() {
        Editor editor = sharedPreferences.edit();
        editor.remove("CART");
        editor.commit();
    }


    public String getOrder() {
        if (sharedPreferences.contains("ORDER"))
            return sharedPreferences.getString("ORDER", null);
        else return null;
    }


    public void setOrder(String order) {
        Editor editor = sharedPreferences.edit();
        editor.putString("ORDER", order);
        editor.commit();
    }

    public void deleteOrder() {
        Editor editor = sharedPreferences.edit();
        editor.remove("ORDER");
        editor.commit();
    }


}

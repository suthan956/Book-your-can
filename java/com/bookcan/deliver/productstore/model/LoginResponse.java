package com.bookcan.deliver.productstore.model;
import com.google.gson.JsonObject;
public class LoginResponse {

    private int response_code;
    private String response;
    private JsonObject user_object;
    private String user_authorization_1;
    private String user_authorization_2;

    public int getResponse_code() {
        return response_code;
    }

    public String getResponse() {
        return response;
    }

    public JsonObject getUser_object() {
        return user_object;
    }
    public String getUser_authorization_1() {
        return user_authorization_1;
    }
    public String getUser_authorization_2() {
        return user_authorization_2;
    }
}

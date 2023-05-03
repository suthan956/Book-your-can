package com.bookcan.deliver.productstore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.adapter.DeliveryAdapter;
import com.bookcan.deliver.productstore.adapter.ProductAdapter;
import com.bookcan.deliver.productstore.fragment.HomeFragment;
import com.bookcan.deliver.productstore.interfaces.BookCanApiService;
import com.bookcan.deliver.productstore.interfaces.DeliveryListEmployeeService;
import com.bookcan.deliver.productstore.model.Bookcanproduct;
import com.bookcan.deliver.productstore.model.OrderList;
import com.bookcan.deliver.productstore.util.Utils;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeActivity extends AppCompatActivity {
LinearLayout logout;
LocalStorage localStorage;
    ProgressDialog progressDialog;
    Retrofit retrofit = null;
    String BASE_URL = "https://bookyourcan.com/";
    List<OrderList> data;
    DeliveryAdapter nAdapter;
    private RecyclerView nRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        logout = findViewById(R.id.logout);
        localStorage = new LocalStorage(getApplicationContext());
        nRecyclerView = findViewById(R.id.new_product_rv);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localStorage.logoutUser();
                startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        connect();
    }

    private void connect() {
        try {
            progressDialog = new ProgressDialog(EmployeeActivity.this);
            progressDialog.setTitle("Book Your Can");
            progressDialog.setMessage("Fetching Products Please Wait.... ");
            progressDialog.show();
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            DeliveryListEmployeeService apiService = retrofit.create(DeliveryListEmployeeService.class);
            LocalStorage localStorage = new LocalStorage(getApplicationContext());
            JSONObject obj = new JSONObject();
            try {
                obj.put("employee_code", new LocalStorage(getApplicationContext()).getCustomerId());
                obj.put("keyword", "employee_order");
                Log.i("response",new LocalStorage(getApplicationContext()).getCustomerId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
            Call<List<OrderList>> call = apiService.getDeliveryList(Utils.App_Authorization_Key,
                    localStorage.getUserAuthorization()[0], localStorage.getUserAuthorization()[1],body);
            call.enqueue(new Callback<List<OrderList>>() {
                @Override
                public void onResponse(Call<List<OrderList>> call, Response<List<OrderList>> response) {
                    try {
                        assert response.body() != null;
                        Log.i("response",response.body().toString());
                        data = response.body();
                        LocalStorage.listOfDelivery = data;
                        nAdapter = new DeliveryAdapter(data, EmployeeActivity.this);
                        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(EmployeeActivity.this,
                                LinearLayoutManager.VERTICAL, false);
                        nRecyclerView.setLayoutManager(nLayoutManager);
                        nRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        nRecyclerView.setAdapter(nAdapter);
                        progressDialog.dismiss();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<OrderList>> call, Throwable throwable) {
                  //  Log.e(TAG, throwable.toString());
                    progressDialog.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finishAffinity();
            finish();
    }

}
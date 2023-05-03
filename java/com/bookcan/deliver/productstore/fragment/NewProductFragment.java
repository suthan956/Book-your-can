package com.bookcan.deliver.productstore.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.adapter.NewProductAdapter;
import com.bookcan.deliver.productstore.adapter.ProductAdapter;
import com.bookcan.deliver.productstore.helper.Data;
import com.bookcan.deliver.productstore.interfaces.BookCanApiService;
import com.bookcan.deliver.productstore.interfaces.CouponApiService;
import com.bookcan.deliver.productstore.model.Bookcanproduct;
import com.bookcan.deliver.productstore.model.DashboardCoupons;
import com.bookcan.deliver.productstore.model.PinCodeResponse;
import com.bookcan.deliver.productstore.util.ApiClient;
import com.bookcan.deliver.productstore.util.Utils;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewProductFragment extends Fragment {
    RecyclerView nRecyclerView;
    DashboardCoupons data;
    private NewProductAdapter nAdapter;
    ProgressDialog progressDialog;
    static Retrofit retrofit = null;
    static final String BASE_URL = "https://bookyourcan.com/";
    String Tag = "new";

    public NewProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new, container, false);
        data = new DashboardCoupons();
        nRecyclerView = view.findViewById(R.id.new_product_rv);
        connect();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Dashboard");
    }

    private void connect() {
        try {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setTitle("Book Your Can");
            progressDialog.setMessage("Fetching Coupons Please Wait.... ");
            progressDialog.show();
            LocalStorage localStorage = new LocalStorage(requireContext());
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("customer_id", localStorage.getCustomerId())
                    .build();
            Call<DashboardCoupons> call = ApiClient.getCouponService().couponCall(Utils.App_Authorization_Key,
                    localStorage.getUserAuthorization()[0], localStorage.getUserAuthorization()[1],requestBody);
            call.enqueue(new Callback<DashboardCoupons>() {
                @Override
                public void onResponse(Call<DashboardCoupons> call, Response<DashboardCoupons> response) {
                    data = response.body();
                    nAdapter = new NewProductAdapter(data, requireContext(), Tag);
                    RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    nRecyclerView.setLayoutManager(nLayoutManager);
                    nRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    nRecyclerView.setAdapter(nAdapter);
                    progressDialog.dismiss();
                }
                @Override
                public void onFailure(Call<DashboardCoupons> call, Throwable throwable) {
                    //Log.e(TAG, throwable.toString());
                    progressDialog.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

}

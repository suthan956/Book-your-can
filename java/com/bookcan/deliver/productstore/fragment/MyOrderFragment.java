package com.bookcan.deliver.productstore.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.activity.BaseActivity;
import com.bookcan.deliver.productstore.adapter.OrderAdapter;
import com.bookcan.deliver.productstore.model.AddToCartResponse;
import com.bookcan.deliver.productstore.model.MyOrderResponse;
import com.bookcan.deliver.productstore.model.Order;
import com.bookcan.deliver.productstore.util.ApiClient;
import com.bookcan.deliver.productstore.util.Utils;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderFragment extends Fragment {
    LocalStorage localStorage;
    LinearLayout linearLayout;
    private List<MyOrderResponse> orderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderAdapter mAdapter;
    ProgressDialog progressDialog;
    public MyOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        progressDialog = new ProgressDialog(requireContext());
        recyclerView = view.findViewById(R.id.order_rv);
        linearLayout = view.findViewById(R.id.no_order_ll);
        checkOrderStatus();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Order History");
    }

    private void checkOrderStatus(){
        showProgressDialog("Fetching Orders...");
        LocalStorage localStorage = new LocalStorage(requireContext());
        JSONObject obj = new JSONObject();
        try {
            obj.put("keyword", "view_order");
            obj.put("customer_id", new LocalStorage(requireContext()).getCustomerId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
        Call<List<MyOrderResponse>> call = ApiClient.getOrderService().myOrderCall(Utils.App_Authorization_Key,
                localStorage.getUserAuthorization()[0], localStorage.getUserAuthorization()[1],body);
        call.enqueue(new Callback<List<MyOrderResponse>>() {
            @Override
            public void onResponse(Call<List<MyOrderResponse>> call, Response<List<MyOrderResponse>> response) {
                progressDialog.dismiss();
                orderList = response.body();
                mAdapter = new OrderAdapter(orderList, getContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                if (orderList.isEmpty()) {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<MyOrderResponse>> call, Throwable throwable) {
                //Log.e(TAG, throwable.toString());
                progressDialog.dismiss();
            }
        });
    }

    public ProgressDialog showProgressDialog(String message) {

        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }
}

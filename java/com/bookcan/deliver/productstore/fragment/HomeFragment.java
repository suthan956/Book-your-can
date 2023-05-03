package com.bookcan.deliver.productstore.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.activity.MainActivity;
import com.bookcan.deliver.productstore.activity.ProductActivity;
import com.bookcan.deliver.productstore.adapter.CategoryAdapter;
import com.bookcan.deliver.productstore.adapter.HomeSliderAdapter;
import com.bookcan.deliver.productstore.adapter.NewProductAdapter;
import com.bookcan.deliver.productstore.adapter.PopularProductAdapter;
import com.bookcan.deliver.productstore.adapter.ProductAdapter;
import com.bookcan.deliver.productstore.helper.Data;
import com.bookcan.deliver.productstore.interfaces.BookCanApiService;
import com.bookcan.deliver.productstore.model.Bookcanproduct;
import com.bookcan.deliver.productstore.model.Category;
import com.bookcan.deliver.productstore.util.Utils;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    List<Bookcanproduct> data;
    ProductAdapter nAdapter;
    private RecyclerView nRecyclerView;
    static final String TAG = MainActivity.class.getSimpleName();
    static final String BASE_URL = "https://bookyourcan.com/";
    static Retrofit retrofit = null;
    ProgressDialog progressDialog;
    String Tag = "List";
    Button toggleBtn;
    private int count = 2;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        nRecyclerView = view.findViewById(R.id.new_product_rv);
        toggleBtn = view.findViewById(R.id.list_to_grid);
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count % 2 == 0) {
                    Tag = "Grid";
                }else {
                    Tag = "List";
                }
                onToggleClicked();
                count++;
            }
        });
        connect();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    private void connect() {
        try {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setTitle("Book Your Can");
            progressDialog.setMessage("Fetching Products Please Wait.... ");
            progressDialog.show();
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            BookCanApiService movieApiService = retrofit.create(BookCanApiService.class);
            LocalStorage localStorage = new LocalStorage(requireContext());
            Call<List<Bookcanproduct>> call = movieApiService.getProduct(Utils.App_Authorization_Key,
                    localStorage.getUserAuthorization()[0], localStorage.getUserAuthorization()[1]);
            call.enqueue(new Callback<List<Bookcanproduct>>() {
                @Override
                public void onResponse(Call<List<Bookcanproduct>> call, Response<List<Bookcanproduct>> response) {
                    try {
                        data = response.body();
                        LocalStorage.listOfProducts = data;
                        nAdapter = new ProductAdapter(data, requireContext(), "grid");
                        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        nRecyclerView.setLayoutManager(nLayoutManager);
                        nRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        nRecyclerView.setAdapter(nAdapter);
                        progressDialog.dismiss();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<Bookcanproduct>> call, Throwable throwable) {
                    Log.e(TAG, throwable.toString());
                    progressDialog.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    public void onToggleClicked() {
        if (Tag.equalsIgnoreCase("List")) {
            setUpRecyclerView();
        } else {
            setUpGridRecyclerView();
        }
    }

    private void setUpGridRecyclerView() {
        nAdapter = new ProductAdapter(data, requireContext(), Tag);
        RecyclerView.LayoutManager nLayoutManager = new GridLayoutManager(getContext(), 2);
        nRecyclerView.setLayoutManager(nLayoutManager);
        nRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nRecyclerView.setAdapter(nAdapter);
    }

    private void setUpRecyclerView() {
        nAdapter = new ProductAdapter(data, requireContext(), Tag);
        RecyclerView.LayoutManager nLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        nRecyclerView.setLayoutManager(nLayoutManager);
        nRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nRecyclerView.setAdapter(nAdapter);
    }
}

package com.bookcan.deliver.productstore.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookcan.deliver.productstore.activity.CheckoutActivity;
import com.bookcan.deliver.productstore.activity.CouponActivity;
import com.bookcan.deliver.productstore.model.AddToCartResponse;
import com.bookcan.deliver.productstore.model.CheckoutResponse;
import com.bookcan.deliver.productstore.util.ApiClient;
import com.bookcan.deliver.productstore.util.Utils;
import com.google.gson.Gson;
import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.activity.BaseActivity;
import com.bookcan.deliver.productstore.activity.CartActivity;
import com.bookcan.deliver.productstore.activity.MainActivity;
import com.bookcan.deliver.productstore.adapter.CheckoutCartAdapter;
import com.bookcan.deliver.productstore.model.Cart;
import com.bookcan.deliver.productstore.model.Order;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmFragment extends Fragment {
    LocalStorage localStorage;
    List<Cart> cartList = new ArrayList<>();
    Gson gson;
    RecyclerView recyclerView;
    CheckoutCartAdapter adapter;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    TextView back, placeOrder;
    TextView total, shipping, totalAmount;
    Double _total, _shipping, _totalAmount;
    ProgressDialog progressDialog;
    List<Order> orderList = new ArrayList<>();
    String orderNo;
    String id;

    public ConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);
        localStorage = new LocalStorage(requireContext());
        recyclerView = view.findViewById(R.id.cart_rv);
        totalAmount = view.findViewById(R.id.total_amount);
        total = view.findViewById(R.id.total);
        shipping = view.findViewById(R.id.shipping_amount);
        back = view.findViewById(R.id.back);
        placeOrder = view.findViewById(R.id.place_order);
        progressDialog = new ProgressDialog(requireContext());
//        gson = new Gson();
//        orderList = ((BaseActivity) getActivity()).getOrderList();
//        Random rnd = new Random();
//        orderNo = "Order #" + (100000 + rnd.nextInt(900000));
//        setUpCartRecyclerview();
//        if (orderList.isEmpty()) {
//            id = "1";
//        } else {
//            id = String.valueOf(orderList.size() + 1);
//        }



        _total = (double) ((CheckoutActivity) Objects.requireNonNull(getActivity())).finalPrice;
        _shipping = 0.0;
        _totalAmount = _total + _shipping;
        total.setText(_total + "");
        shipping.setText(_shipping + "");
        totalAmount.setText(_totalAmount + "");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CartActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progressDialog.setMessage("Please Wait....");
//                progressDialog.show();
                //((CheckoutActivity) Objects.requireNonNull(getActivity())).startPayment(_totalAmount + "");
                checkOutProduct();
            }
        });



        return view;
    }


    private void showCustomDialog() {

        // Create custom dialog object
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        // Include dialog.xml file
        dialog.setContentView(R.layout.success_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });
        // Set dialog title

        dialog.show();
    }

    private void setUpCartRecyclerview() {

        cartList = new ArrayList<>();
        cartList = ((BaseActivity) getContext()).getCartList();


        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        adapter = new CheckoutCartAdapter(cartList, getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Confirm");
    }

    private void checkOutProduct(){
        //showProgressDialog("Adding Item To the Cart...");
        progressDialog.setMessage("Processing Checkout...");
        progressDialog.show();
        LocalStorage localStorage = new LocalStorage(requireContext());
        JSONObject obj = new JSONObject();
        try {
            obj.put("keyword", "product_can_order");
            obj.put("customer_id", new LocalStorage(requireContext()).getCustomerId());
            obj.put("grand_total", ""+_total);
            obj.put("payment_status", "txn_success");
            obj.put("delivery_slot", ((CheckoutActivity) Objects.requireNonNull(getActivity())).delSlot);
            obj.put("order_type", "buy_cart_product");
            obj.put("txn_id", "");
            obj.put("mode_of_payment", "COD");
            obj.put("response_code", "");
            obj.put("response_msg", "");
            obj.put("gateway_name", "");
            obj.put("bank_txn_id", "");
            obj.put("bank_name", "");
            obj.put("merchant_id", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
        Call<CheckoutResponse> call = ApiClient.getCheckoutService().addCartCall(Utils.App_Authorization_Key,
                localStorage.getUserAuthorization()[0], localStorage.getUserAuthorization()[1],body);
        call.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                progressDialog.dismiss();
                if(response.body().getMessage().contains("success")) {
                    showCustomDialog();
                }else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                    alertDialog.setTitle("Order Status");
                    alertDialog.setMessage(response.body().getMessage());
                    alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }
                    });
                    alertDialog.show();
                }
            }
            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable throwable) {
                //Log.e(TAG, throwable.toString());
                progressDialog.dismiss();
                Toast.makeText(requireContext(),"Checkout Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

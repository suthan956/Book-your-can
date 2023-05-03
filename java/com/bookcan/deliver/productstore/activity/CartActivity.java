package com.bookcan.deliver.productstore.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookcan.deliver.productstore.interfaces.updateCartDashboard;
import com.bookcan.deliver.productstore.model.AddToCartResponse;
import com.bookcan.deliver.productstore.model.CartDashboard;
import com.bookcan.deliver.productstore.model.CartProduct;
import com.bookcan.deliver.productstore.model.CheckoutResponse;
import com.bookcan.deliver.productstore.util.ApiClient;
import com.bookcan.deliver.productstore.util.Utils;
import com.google.gson.Gson;
import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.adapter.CartAdapter;
import com.bookcan.deliver.productstore.model.Cart;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;
import com.google.gson.JsonArray;

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


public class CartActivity extends BaseActivity implements updateCartDashboard {
    LocalStorage localStorage;
    List<CartProduct> cartList = new ArrayList<>();
    Gson gson;
    RecyclerView recyclerView;
    CartAdapter adapter;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    ImageView emptyCart;
    LinearLayout checkoutLL;
    TextView totalPrice;
    private String mState = "SHOW_MENU";
    ProgressDialog progressDialog;
    private int finalPrice = 0;
    public static String MrngOrEVeSelected = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        progressDialog = new ProgressDialog(CartActivity.this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        changeActionBarTitle(getSupportActionBar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        //upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        localStorage = new LocalStorage(getApplicationContext());
        gson = new Gson();
        emptyCart = findViewById(R.id.empty_cart_img);
        checkoutLL = findViewById(R.id.checkout_LL);
        totalPrice = findViewById(R.id.total_price);
      //  totalPrice.setText("Rs. " + getTotalPrice() + "");
     //   setUpCartRecyclerview();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCartProduct();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        MenuItem item = menu.findItem(R.id.cart_delete);
        item.setVisible(true);
//        if (mState.equalsIgnoreCase("HIDE_MENU")) {
//            item.setVisible(false);
//        } else {
//            item.setVisible(true);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart_delete:

//                AlertDialog diaBox = showDeleteDialog();
//                diaBox.show();

                return true;

            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AlertDialog showDeleteDialog() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)

                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        localStorage.deleteCart();
                        adapter.notifyDataSetChanged();
                        emptyCart.setVisibility(View.VISIBLE);
                        mState = "HIDE_MENU";
                        invalidateOptionsMenu();
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;
    }


    private void changeActionBarTitle(ActionBar actionBar) {
        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        TextView tv = new TextView(getApplicationContext());
        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(null, Typeface.BOLD);
        // Set text to display in TextView
        tv.setText("Cart"); // ActionBar title text
        tv.setTextSize(20);

        // Set the text color of TextView to red
        // This line change the ActionBar title text color
        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Set the ActionBar display option
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // Finally, set the newly created TextView as ActionBar custom view
        actionBar.setCustomView(tv);
    }


    private void setUpCartRecyclerview() {

    }


    public void onCheckoutClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
        intent.putExtra("finalPrice",finalPrice);
        intent.putExtra("delivery_slot",MrngOrEVeSelected);
        startActivity(intent);
    }


    @Override
    public void updateTotalPrice() {

        //totalPrice.setText("Rs. " + getTotalPrice() + "");
        if (getTotalPrice() == 0.0) {
            mState = "HIDE_MENU";
            invalidateOptionsMenu();
            emptyCart.setVisibility(View.VISIBLE);
            checkoutLL.setVisibility(View.GONE);
        }
    }

    private void getCartProduct(){
        showProgressDialog("Fetching Cart Items...");
        LocalStorage localStorage = new LocalStorage(getApplicationContext());
        JSONObject obj = new JSONObject();
        try {
            obj.put("keyword", "cart_products");
            obj.put("customer_id", new LocalStorage(getApplicationContext()).getCustomerId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String[] mrngDel = {"Morning: 09:00-12:00"};
        final String[] eveDel = {"Evening: 02:00-08:00"};
        //RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
        Call<ArrayList<String>> callDelTime = ApiClient.getDeliveryService().deliveryCall(Utils.App_Authorization_Key,
                localStorage.getUserAuthorization()[0], localStorage.getUserAuthorization()[1]);

        callDelTime.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
             try {
                 ArrayList<String> arrayList = response.body();
                 if (arrayList!=null) {
                     mrngDel[0] = arrayList.get(0);
                     eveDel[0] = arrayList.get(2);
                 }
             }catch (Exception e){
                 e.printStackTrace();
             }
            }
            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
        Call<CartDashboard> call = ApiClient.getCartDashboardService().addCartCall(Utils.App_Authorization_Key,
                localStorage.getUserAuthorization()[0], localStorage.getUserAuthorization()[1],body);
        call.enqueue(new Callback<CartDashboard>() {
            @Override
            public void onResponse(Call<CartDashboard> call, Response<CartDashboard> response) {
                try {
                    progressDialog.dismiss();
                    cartList = new ArrayList<>();
                    cartList = response.body().getCoupons();
                    if (cartList.isEmpty()) {
                        mState = "HIDE_MENU";
                        invalidateOptionsMenu();
                        emptyCart.setVisibility(View.VISIBLE);
                        checkoutLL.setVisibility(View.GONE);
                    }
                    recyclerView = findViewById(R.id.cart_rv);
                    recyclerView.setHasFixedSize(true);
                    recyclerViewlayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(recyclerViewlayoutManager);
                    if (cartList.size()>0) {
                        cartList.add(new CartProduct(response.body().getDelivery_charge(), response.body().getLift_charge(), response.body().getTotal_pay(), "", mrngDel[0],
                                eveDel[0], "extra_item_bill", response.body().getGrand_total(), "", "", "", "",
                                "", "", "", "",
                                "", ""));
                    }
                    adapter = new CartAdapter(cartList, CartActivity.this);
                    recyclerView.setAdapter(adapter);
                    totalPrice.setText("Rs. " + response.body().getTotal_pay());
                    finalPrice = Integer.parseInt(response.body().getGrand_total());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<CartDashboard> call, Throwable throwable) {
                //Log.e(TAG, throwable.toString());
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(),"Not Added",Toast.LENGTH_SHORT).show();
            }
        });

    }


    public ProgressDialog showProgressDialog(String message) {

        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    @Override
    public void refreshCart() {
        getCartProduct();
    }
    @Override
    public void startProgress(String msg) {
        showProgressDialog(msg);
    }
}

package com.bookcan.deliver.productstore.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.customfonts.MyTextView;
import com.bookcan.deliver.productstore.model.CheckoutResponse;
import com.bookcan.deliver.productstore.util.ApiClient;
import com.bookcan.deliver.productstore.util.Utils;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponActivity extends AppCompatActivity {

    TextView couponName, totalCoupon, couponAvail;
    RadioButton mrngDel, eveDel;
    public TextView quantity,quantity1, inc, dec, inc1, dec1;
    private String MrngOrEVeSelected = "0";
    ImageView imageView;
    String _id, _title, _image, _description, _price, _currency, _discount, _attribute, _brand, _productCode,
            _brandTypeId, _productDesc, _replaceId;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    int total_item = 0;
    MyTextView buy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        changeActionBarTitle(getSupportActionBar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        //upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        progressDialog = new ProgressDialog(CouponActivity.this);
        TextView term = findViewById(R.id.apv_term);
        term.setText(termText());
        couponName = findViewById(R.id.apv_currency);
        totalCoupon = findViewById(R.id.apv_price);
        couponAvail = findViewById(R.id.apv_price1);
        mrngDel = findViewById(R.id.mrngDel);
        eveDel = findViewById(R.id.eveDel);
        imageView = findViewById(R.id.apv_image);
        progressBar = findViewById(R.id.progressbar);
        inc = findViewById(R.id.quantity_plus);
        dec = findViewById(R.id.quantity_minus);
        quantity = findViewById(R.id.quantity);
        buy = findViewById(R.id.buy);

        Intent intent = getIntent();
        _id = intent.getStringExtra("id");
        _title = intent.getStringExtra("title");
        _image = intent.getStringExtra("image");
        _price = intent.getStringExtra("price");
        _productCode= intent.getStringExtra("totalCoupon");
        _productDesc= intent.getStringExtra("availCoupon");

        couponName.setText(_title);
        totalCoupon.setText(_productCode);
        couponAvail.setText(_productDesc);

        mrngDel.setChecked(true);
        mrngDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mrngDel.setChecked(true);
                eveDel.setChecked(false);
                MrngOrEVeSelected = "0";
            }
        });
        eveDel.setChecked(false);
        eveDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mrngDel.setChecked(false);
                eveDel.setChecked(true);
                MrngOrEVeSelected = "1";
            }
        });

        try {

            if (_image != null) {
                Picasso.get().load(_image).error(R.drawable.no_image).into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total_item<50) {
                    total_item++;
                    quantity.setText("" + total_item);
                }else {
                    Toast.makeText(getApplicationContext(),"Sorry, You have selected max count",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total_item > 0) {
                    total_item--;
                    quantity.setText("" + total_item);
                }
            }
        });


        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total_item > 0 ) {
                    checkOutProduct();
                } else {
                    Toast.makeText(getApplicationContext(), "Quantity Not Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
               onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String termText(){
        return  "1. Products can be purchased via paytm , COD , Cash on Card .\n" +
                " 2. By Default , you can replace your old can in order to buy new one .\n" +
                " 3. You cannot exchange local branded empty cans with branded ones ( eg If you want bisleri product you can exchange only with bisleri empty can ) .\n" +
                " 4. Incase if you want to buy can and water together , you can choose the option the above . It may cost extra amount based on the brand you chosen .\n";
    }

    private void changeActionBarTitle(ActionBar actionBar) {
        // Create a LayoutParams for TextView
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
        TextView tv = new TextView(getApplicationContext());
        // Apply the layout parameters to TextView widget
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.LEFT);
        tv.setTypeface(null, Typeface.BOLD);
        // Set text to display in TextView
        tv.setText("Redeem Coupon"); // ActionBar title text
        tv.setTextSize(20);

        // Set the text color of TextView to red
        // This line change the ActionBar title text color
        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Set the ActionBar display option
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // Finally, set the newly created TextView as ActionBar custom view
        actionBar.setCustomView(tv);
    }

    private void checkOutProduct(){
        //showProgressDialog("Adding Item To the Cart...");
        progressDialog.setMessage("Processing Checkout...");
        progressDialog.show();
        LocalStorage localStorage = new LocalStorage(getApplicationContext());
        JSONObject obj = new JSONObject();
        try {
            obj.put("keyword", "coupon_order_product");
            obj.put("customer_id", new LocalStorage(getApplicationContext()).getCustomerId());
            obj.put("coupon_id", _id);
            obj.put("use_coupon_quantity", total_item);
            obj.put("delivery_slot", MrngOrEVeSelected);
            obj.put("order_type", "buy_coupon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
        Call<CheckoutResponse> call = ApiClient.getCouponCheckoutService().addCouponCartCall(Utils.App_Authorization_Key,
                localStorage.getUserAuthorization()[0], localStorage.getUserAuthorization()[1],body);
        call.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                progressDialog.dismiss();
                if(response.body().getMessage().contains("success")) {
                    showCustomDialog();
                }else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CouponActivity.this);
                    alertDialog.setTitle("Order Status");
                    alertDialog.setMessage(response.body().getMessage());
                    alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    });
                    alertDialog.show();
                }
            }
            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable throwable) {
                //Log.e(TAG, throwable.toString());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Checkout Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCustomDialog() {

        // Create custom dialog object
        final Dialog dialog = new Dialog(CouponActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        // Include dialog.xml file
       dialog.setContentView(R.layout.success_dialog);
       //dialog.setTitle(msg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        // Set dialog title

        dialog.show();
    }
}
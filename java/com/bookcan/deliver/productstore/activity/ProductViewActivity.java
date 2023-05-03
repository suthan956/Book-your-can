package com.bookcan.deliver.productstore.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.adapter.NewProductAdapter;
import com.bookcan.deliver.productstore.adapter.ProductAdapter;
import com.bookcan.deliver.productstore.customfonts.MyTextView;
import com.bookcan.deliver.productstore.helper.Converter;
import com.bookcan.deliver.productstore.interfaces.BookCanApiService;
import com.bookcan.deliver.productstore.model.AddToCartResponse;
import com.bookcan.deliver.productstore.model.Bookcanproduct;
import com.bookcan.deliver.productstore.model.Cart;
import com.bookcan.deliver.productstore.model.DashboardCoupons;
import com.bookcan.deliver.productstore.util.ApiClient;
import com.bookcan.deliver.productstore.util.Utils;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProductViewActivity extends BaseActivity {
    private static int cart_count = 0;
    public TextView quantity,quantity1, inc, dec, inc1, dec1;
    String _id, _title, _image, _description, _price, _currency, _discount, _attribute ,_brand, _productCode,
            _brandTypeId,_productDesc,_replaceId;
    TextView id, title, description, price, currency, discount, attribute,brand,addToCart,term;
    ImageView imageView;
    ProgressBar progressBar;
    LinearLayout  share;
    RelativeLayout quantityLL;
    List<Cart> cartList = new ArrayList<>();
    int total_item = 0;
    int total_item_1 = 0;
    MyTextView buy;
    private boolean isAddCart = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        progressDialog = new ProgressDialog(ProductViewActivity.this);
        Intent intent = getIntent();
        addToCart = findViewById(R.id.add_to_cart_ll);
        buy = findViewById(R.id.buy);
        if (intent.getStringExtra("Clicked_From")
                .equalsIgnoreCase("cart_click")) {
            isAddCart = false;
            addToCart.setText("UPDATE CART");
            buy.setVisibility(View.GONE);
        }else {
            isAddCart = true;
            addToCart.setText("ADD TO CART");
            buy.setVisibility(View.VISIBLE);
        }
        _id = intent.getStringExtra("id");
        _title = intent.getStringExtra("title");
        _image = intent.getStringExtra("image");
        _description = intent.getStringExtra("description");
        _price = intent.getStringExtra("price");
        _currency = intent.getStringExtra("currency");
        _discount = intent.getStringExtra("discount");
        _attribute = intent.getStringExtra("attribute");
        _brand = intent.getStringExtra("brand");
        _productCode = intent.getStringExtra("productCode");
        _brandTypeId = intent.getStringExtra("brandTypeId");
        _productDesc = intent.getStringExtra("productDesc");
        _replaceId = intent.getStringExtra("replaceId");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        changeActionBarTitle(getSupportActionBar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        //upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        cart_count = cartCount();

        title = findViewById(R.id.apv_title);
        description = findViewById(R.id.apv_description);
        currency = findViewById(R.id.apv_currency);
        price = findViewById(R.id.apv_price);
        attribute = findViewById(R.id.apv_attribute);
        brand = findViewById(R.id.brand);
        discount = findViewById(R.id.apv_discount);
        imageView = findViewById(R.id.apv_image);
        progressBar = findViewById(R.id.progressbar);
        share = findViewById(R.id.apv_share);
        quantityLL = findViewById(R.id.quantity_rl);
        quantity = findViewById(R.id.quantity);
        quantity1 = findViewById(R.id.quantity1);
        inc = findViewById(R.id.quantity_plus);
        dec = findViewById(R.id.quantity_minus);
        inc1 = findViewById(R.id.quantity_plus1);
        dec1 = findViewById(R.id.quantity_minus1);
        term = findViewById(R.id.apv_term);
        term.setText(termText());
        cartList = getCartList();
        title.setText(_title);
        description.setText(_description);
        price.setText(_price);
        currency.setText(_currency);
        currency.setPaintFlags(currency.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        attribute.setText(_attribute+ " Litre");
        discount.setText(_discount);
        brand.setText(_brand);
        Log.d(TAG, "Discount : " + _discount);
//        if (_discount != null || _discount.length() != 0 || _discount != "") {
//            discount.setVisibility(View.VISIBLE);
//        } else {
//            discount.setVisibility(View.GONE);
//        }

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


        }catch (Exception e){
        e.printStackTrace();
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEntry = _image + "\n" + _title + "\n" + _description + "\n" + _attribute + "-" + _currency + _price + "(" + _discount + ")";

                Intent textShareIntent = new Intent(Intent.ACTION_SEND);
                textShareIntent.putExtra(Intent.EXTRA_TEXT, userEntry);
                textShareIntent.setType("text/plain");
                startActivity(textShareIntent);
            }
        });


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total_item > 0 || total_item_1 > 0) {
                    addProduct(false);
                }else {
                    Toast.makeText(getApplicationContext(),"Quantity Not Added",Toast.LENGTH_SHORT).show();
                }
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total_item > 0 || total_item_1 > 0) {
                    addProduct(true);
                } else {
                    Toast.makeText(getApplicationContext(), "Quantity Not Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        inc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total_item_1<50) {
                    total_item_1++;
                    quantity1.setText("" + total_item_1);
                }else {
                    Toast.makeText(getApplicationContext(),"Sorry, You have selected max count",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dec1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total_item_1 > 0) {
                    total_item_1--;
                    quantity1.setText("" + total_item_1);
                }
            }
        });


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
        tv.setText(_title); // ActionBar title text
        tv.setTextSize(20);

        // Set the text color of TextView to red
        // This line change the ActionBar title text color
        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Set the ActionBar display option
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // Finally, set the newly created TextView as ActionBar custom view
        actionBar.setCustomView(tv);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

//                Intent intent = new Intent(ProductViewActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
                onBackPressed();
                return true;

            case R.id.cart_action:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(ProductViewActivity.this, cart_count, R.drawable.ic_shopping_cart_black_24dp));
        return true;
    }


    @Override
    public void onAddProduct() {
        cart_count++;
        invalidateOptionsMenu();

    }

    private void addProduct(boolean isCheckoutClicked){
        boolean addOneByOne = total_item > 0 && total_item_1 > 0;
        showProgressDialog("Adding Item To the Cart...");
        LocalStorage localStorage = new LocalStorage(getApplicationContext());
        JSONObject obj = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            if (isAddCart) {
                obj.put("keyword", "add_cart");
            }else {
                obj.put("keyword", "update_cart");
            }
            obj.put("customer_id", new LocalStorage(getApplicationContext()).getCustomerId());
            obj.put("product_code", _productCode);
        //for(int i = 0; i < 10; i++){
            JSONObject objFromArray = new JSONObject();
                if (total_item > 0) {
                    objFromArray.put("price_type_id", _replaceId);
                    objFromArray.put("quantity", total_item + "");
                } else if (total_item_1 > 0) {
                    objFromArray.put("price_type_id", _brandTypeId);
                    objFromArray.put("quantity", total_item_1 + "");
                }
            array.put(objFromArray);
        //}
        obj.put("products", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
        Call<AddToCartResponse> call = ApiClient.getAddCartService().addCartCall(Utils.App_Authorization_Key,
                localStorage.getUserAuthorization()[0], localStorage.getUserAuthorization()[1],body);
        call.enqueue(new Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                if (addOneByOne){
                    total_item = 0;
                    addProduct(isCheckoutClicked);
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (isCheckoutClicked) {
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                    } else {
                        onBackPressed();
                    }
                }
            }
            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable throwable) {
                //Log.e(TAG, throwable.toString());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Not Added",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRemoveProduct() {
        cart_count--;
        invalidateOptionsMenu();

    }

    public ProgressDialog showProgressDialog(String message) {

        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    private String termText(){
        return  "1. Products can be purchased via paytm , COD , Cash on Card .\n" +
                " 2. By Default , you can replace your old can in order to buy new one .\n" +
                " 3. You cannot exchange local branded empty cans with branded ones ( eg If you want bisleri product you can exchange only with bisleri empty can ) .\n" +
                " 4. Incase if you want to buy can and water together , you can choose the option the above . It may cost extra amount based on the brand you chosen .\n";
    }
}

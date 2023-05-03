package com.bookcan.deliver.productstore.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bookcan.deliver.productstore.activity.ProductViewActivity;
import com.bookcan.deliver.productstore.interfaces.updateCartDashboard;
import com.bookcan.deliver.productstore.model.AddToCartResponse;
import com.bookcan.deliver.productstore.model.CartProduct;
import com.bookcan.deliver.productstore.util.ApiClient;
import com.bookcan.deliver.productstore.util.Utils;
import com.google.gson.Gson;
import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.activity.CartActivity;
import com.bookcan.deliver.productstore.model.Cart;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    List<CartProduct> cartList;
    Context context;
    int pQuantity = 1;
    String _subtotal, _price, _quantity;
    LocalStorage localStorage;
    Gson gson;
    private updateCartDashboard refreshCart;
    int total_item = -1;

    public CartAdapter(List<CartProduct> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
        try {
            refreshCart = (updateCartDashboard) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final CartProduct cart = cartList.get(position);
        try {
            localStorage = new LocalStorage(context);
            gson = new Gson();
            if (cart.getProduct_name().equalsIgnoreCase("extra_item_bill")) {
                holder.billItem.setVisibility(View.VISIBLE);
                holder.mrngDel.setChecked(true);
                holder.mrngDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.mrngDel.setChecked(true);
                        holder.eveDel.setChecked(false);
                        CartActivity.MrngOrEVeSelected = "0";
                    }
                });
                holder.eveDel.setChecked(false);
                holder.eveDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.mrngDel.setChecked(false);
                        holder.eveDel.setChecked(true);
                        CartActivity.MrngOrEVeSelected = "1";
                    }
                });
                holder.mrngDel.setText(cart.getProduct_id());
                holder.eveDel.setText(cart.getProduct_code());
                holder.cardView.setVisibility(View.GONE);
                holder.delCharge.setText("Delivery Charge : " + cart.getReplacement_id());
                holder.liftCharge.setText("Lift Charge : " + cart.getCan_id());
                holder.toPay.setText("To Pay : Rs. " + cart.getCustomer_id());
            } else {
                holder.cardView.setVisibility(View.VISIBLE);
                holder.billItem.setVisibility(View.GONE);
                holder.title.setText(cart.getProduct_name());
                _price = cart.getFinal_price();
                _quantity = cart.getTotal_quantity();

                holder.quantity.setText(_quantity);
                holder.price.setText(_price);
                holder.currency.setText("Rs.");
                _subtotal = cart.getTotal_price();//String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));
                holder.attribute.setText("Total Price: Rs. " + _subtotal);
                holder.subTotal.setText(_subtotal);

                holder.exCanCost.setText("Cost Of Exchange Can : " + cart.getTotal_replacement_price()
                        + "(" + cart.getTotal_replacement_quantity() + " Nos )");
                holder.newCanCost.setText("Cost Of New Can : " + cart.getTotal_can_price()
                        + "(" + cart.getTotal_can_quantity() + " Nos )");

                Picasso.get()
                        .load(cart.getImage())
                        .into(holder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.d("Error : ", e.getMessage());
                            }
                        });

                holder.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            total_item = Integer.parseInt(cart.getTotal_quantity());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        total_item = total_item + 1;
                        updateOrDeleteCart("UpdateCart", cart.getProduct_code(),
                                cart.getCan_id(), "" + total_item);

                    }
                });
                holder.minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            total_item = Integer.parseInt(cart.getTotal_quantity());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        total_item = total_item - 1;
                        if (total_item == 0 || total_item < 1) {
                            updateOrDeleteCart("DeleteCart", cart.getProduct_code(),
                                    cart.getCan_id(), "" + total_item);
                        } else {
                            updateOrDeleteCart("UpdateCart", cart.getProduct_code(),
                                    cart.getCan_id(), "" + total_item);
                        }
                    }
                });

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateOrDeleteCart("DeleteCart", cart.getProduct_code(),
                                cart.getCan_id(), "" + total_item);

                    }
                });

                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ProductViewActivity.class);
                        intent.putExtra("Clicked_From", "cart_click");
                        intent.putExtra("id", cart.getProduct_id());
                        intent.putExtra("title", cart.getProduct_name());
                        intent.putExtra("image", cart.getImage());
                        intent.putExtra("price", cart.getFinal_price());
                        intent.putExtra("currency", cart.getActual_price());
                        intent.putExtra("attribute", cart.getProduct_litre());
                        intent.putExtra("discount", "");
                        intent.putExtra("brand", cart.getBrand_type());
                        intent.putExtra("productCode", cart.getProduct_code());
                        intent.putExtra("description", "");
                        intent.putExtra("brandTypeId", cart.getCan_id());
                        intent.putExtra("replaceId", cart.getReplacement_id());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                });


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        ProgressBar progressBar;
        CardView cardView;
        TextView offer, currency, price, quantity, attribute, addToCart, subTotal,exCanCost,newCanCost,delCharge,
                liftCharge,toPay;
        Button plus, minus, delete, edit;
        LinearLayout billItem;
        RadioButton mrngDel,eveDel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            exCanCost = itemView.findViewById(R.id.exchangeCanQty);
            newCanCost = itemView.findViewById(R.id.newCanQty);
            delCharge = itemView.findViewById(R.id.deliverChg);
            liftCharge = itemView.findViewById(R.id.liftChg);
            toPay = itemView.findViewById(R.id.toPay);

            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            progressBar = itemView.findViewById(R.id.progressbar);
            quantity = itemView.findViewById(R.id.quantity);
            currency = itemView.findViewById(R.id.product_currency);
            attribute = itemView.findViewById(R.id.product_attribute);
            plus = itemView.findViewById(R.id.quantity_plus);
            minus = itemView.findViewById(R.id.quantity_minus);
            delete = itemView.findViewById(R.id.cart_delete);
            edit = itemView.findViewById(R.id.cart_edit);
            subTotal = itemView.findViewById(R.id.sub_total);
            price = itemView.findViewById(R.id.product_price);
            billItem = itemView.findViewById(R.id.bill_item);
            cardView = itemView.findViewById(R.id.card_view);
            mrngDel = itemView.findViewById(R.id.mrngDel);
            eveDel = itemView.findViewById(R.id.eveDel);
        }
    }

    private void updateOrDeleteCart(String type,String _productCode,String _brandTypeId,String total_item){
        //showProgressDialog("Adding Item To the Cart...");
        LocalStorage localStorage = new LocalStorage(context);
        JSONObject obj = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            if(type.equals("DeleteCart")) {
                obj.put("keyword", "delete_cart");
                obj.put("customer_id", new LocalStorage(context).getCustomerId());
                obj.put("product_code", _productCode);
                refreshCart.startProgress("Deleting Product...");
            } else if (type.equals("UpdateCart")) {
                obj.put("keyword", "update_cart");
                obj.put("customer_id", new LocalStorage(context).getCustomerId());
                obj.put("product_code", _productCode);
                //for(int i = 0; i < 10; i++){
                JSONObject objFromArray = new JSONObject();
                objFromArray.put("price_type_id", _brandTypeId);
                objFromArray.put("quantity", total_item + "");
                array.put(objFromArray);
                obj.put("products", array);
                refreshCart.startProgress("Updating Product...");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),obj.toString());
        Call<AddToCartResponse> call = ApiClient.getAddCartService().addCartCall(Utils.App_Authorization_Key,
                localStorage.getUserAuthorization()[0], localStorage.getUserAuthorization()[1],body);
        call.enqueue(new retrofit2.Callback<AddToCartResponse>() {
            @Override
            public void onResponse(Call<AddToCartResponse> call, Response<AddToCartResponse> response) {
                refreshCart.refreshCart();
            }
            @Override
            public void onFailure(Call<AddToCartResponse> call, Throwable throwable) {

            }
        });
    }
}

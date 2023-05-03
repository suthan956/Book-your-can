package com.bookcan.deliver.productstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bookcan.deliver.productstore.activity.CouponActivity;
import com.bookcan.deliver.productstore.model.Bookcancoupons;
import com.bookcan.deliver.productstore.model.DashboardCoupons;
import com.google.gson.Gson;
import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.activity.BaseActivity;
import com.bookcan.deliver.productstore.activity.MainActivity;
import com.bookcan.deliver.productstore.activity.ProductViewActivity;
import com.bookcan.deliver.productstore.interfaces.AddorRemoveCallbacks;
import com.bookcan.deliver.productstore.model.Cart;
import com.bookcan.deliver.productstore.model.Product;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

 
public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.MyViewHolder> {

    DashboardCoupons productList;
    Context context;
    String Tag;

    LocalStorage localStorage;
    Gson gson;
    List<Cart> cartList = new ArrayList<>();
    String _quantity, _price, _attribute, _subtotal;

    public NewProductAdapter(DashboardCoupons productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public NewProductAdapter(DashboardCoupons productList, Context context, String tag) {
        this.productList = productList;
        this.context = context;
        Tag = tag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;
        if (Tag.equalsIgnoreCase("Home")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_new_home_products, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_new_products, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Bookcancoupons product = productList.getCoupons().get(position);
        localStorage = new LocalStorage(context);
        gson = new Gson();
        cartList = ((BaseActivity) context).getCartList();
        holder.quantity.setText("1");

        holder.title.setText(product.getCoupon_name());
        holder.price.setText(product.getPending_available_cans());
        holder.currency.setText(product.getActual_price());
        holder.attribute.setText("Total Coupons: "+product.getTotal_cans());
        Picasso.get().load(product.getCoupon_image()).error(R.drawable.no_image).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Log.d("Error : ", e.getMessage());
            }
        });

//        if (!cartList.isEmpty()) {
//            for (int i = 0; i < cartList.size(); i++) {
//                if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
//                    holder.shopNow.setVisibility(View.GONE);
//                    holder.quantity_ll.setVisibility(View.VISIBLE);
//                    holder.quantity.setText(cartList.get(i).getQuantity());
//
//                }
//            }
//        }


//        holder.shopNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.shopNow.setVisibility(View.GONE);
//                holder.quantity_ll.setVisibility(View.VISIBLE);
//                _price = product.getPrice();
//                _quantity = holder.quantity.getText().toString();
//                _attribute = product.getAttribute();
//                _subtotal = String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));
//
//                if (context instanceof MainActivity) {
//                    Cart cart = new Cart(product.getId(), product.getTitle(), product.getImage(), product.getCurrency(), _price, _attribute, _quantity, _subtotal);
//                    cartList = ((BaseActivity) context).getCartList();
//                    cartList.add(cart);
//
//                    String cartStr = gson.toJson(cartList);
//                    //Log.d("CART", cartStr);
//                    localStorage.setCart(cartStr);
//                    ((AddorRemoveCallbacks) context).onAddProduct();
//                    notifyItemChanged(position);
//                }
//            }
//        });
//
//
//        holder.plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                for (int i = 0; i < cartList.size(); i++) {
//                    if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
//                        int total_item = Integer.parseInt(cartList.get(i).getQuantity());
//                        total_item++;
//                        Log.d("totalItem", total_item + "");
//                        holder.quantity.setText(total_item + "");
//                        _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
//                        cartList.get(i).setQuantity(holder.quantity.getText().toString());
//                        cartList.get(i).setSubTotal(_subtotal);
//                        String cartStr = gson.toJson(cartList);
//                        //Log.d("CART", cartStr);
//                        localStorage.setCart(cartStr);
//                    }
//                }
//
//
//            }
//        });
//
//        holder.minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Integer.parseInt(holder.quantity.getText().toString()) != 1) {
//                    for (int i = 0; i < cartList.size(); i++) {
//                        if (cartList.get(i).getId().equalsIgnoreCase(product.getId())) {
//                            int total_item = Integer.parseInt(holder.quantity.getText().toString());
//
//                            total_item--;
//                            holder.quantity.setText(total_item + "");
//                            Log.d("totalItem", total_item + "");
//
//                            _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
//
//                            cartList.get(i).setQuantity(holder.quantity.getText().toString());
//                            cartList.get(i).setSubTotal(_subtotal);
//                            String cartStr = gson.toJson(cartList);
//                            //Log.d("CART", cartStr);
//                            localStorage.setCart(cartStr);
//
//                        }
//                    }
//
//                }
//
//
//            }
//        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CouponActivity.class);
                intent.putExtra("id", product.getCoupon_id());
                intent.putExtra("title", product.getCoupon_name());
                intent.putExtra("image", product.getCoupon_image());
                intent.putExtra("price", product.getFinal_price());
                intent.putExtra("currency", "Rs.");
                intent.putExtra("totalCoupon", product.getTotal_cans());
                intent.putExtra("availCoupon", product.getPending_available_cans());
                //intent.putExtra("description", product.getDescription());


                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return productList.getCoupons().size();

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, attribute, currency, price, shopNow;
        ProgressBar progressBar;
        LinearLayout quantity_ll;
        TextView plus, minus, quantity;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            attribute = itemView.findViewById(R.id.product_attribute);
            price = itemView.findViewById(R.id.product_price);
            currency = itemView.findViewById(R.id.product_currency);
            shopNow = itemView.findViewById(R.id.shop_now);
            progressBar = itemView.findViewById(R.id.progressbar);
            quantity_ll = itemView.findViewById(R.id.quantity_ll);
            quantity = itemView.findViewById(R.id.quantity);
            plus = itemView.findViewById(R.id.quantity_plus);
            minus = itemView.findViewById(R.id.quantity_minus);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }
}

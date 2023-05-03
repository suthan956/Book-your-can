package com.bookcan.deliver.productstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookcan.deliver.productstore.model.MyOrderResponse;
import com.google.gson.Gson;
import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.model.Order;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;

import java.util.List;

 
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    List<MyOrderResponse> orderList;
    Context context;
    int pQuantity = 1;
    String _subtotal, _price, _quantity;
    LocalStorage localStorage;
    Gson gson;

    public OrderAdapter(List<MyOrderResponse> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final MyOrderResponse order = orderList.get(position);
        holder.orderId.setText("order Id : "+order.getOrder_id());
        holder.date.setText("Date     : "+order.getUpdated_at());
        holder.total.setText("Total    : "+order.getGrand_total());
        holder.status.setText("Delivery Status : "+order.getDelivery_status());


    }

    @Override
    public int getItemCount() {

        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, date, total, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.order_id);
            date = itemView.findViewById(R.id.date);
            total = itemView.findViewById(R.id.total_amount);
            status = itemView.findViewById(R.id.status);

        }
    }
}

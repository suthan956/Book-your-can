package com.bookcan.deliver.productstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.activity.DeliverViewActivity;
import com.bookcan.deliver.productstore.activity.ProductViewActivity;
import com.bookcan.deliver.productstore.model.MyOrderResponse;
import com.bookcan.deliver.productstore.model.OrderList;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;
import com.google.gson.Gson;

import java.util.List;


public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.MyViewHolder> {

    List<OrderList> orderList;
    Context context;
    int pQuantity = 1;
    String _subtotal, _price, _quantity;
    LocalStorage localStorage;
    Gson gson;

    public DeliveryAdapter(List<OrderList> orderList, Context context) {
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

        final OrderList order = orderList.get(position);
        holder.orderId.setText(order.getCustomer_name()+"\n"+order.getMobile());
        holder.date.setText("Payment Mode     : "+order.getMode_of_payment());
        holder.total.setText("Location    : "+order.getAddress());
        holder.status.setText("Distance : "+order.getDistance()+"Kms");
        holder.deliverItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeliverViewActivity.class);
                intent.putExtra("order_id", order.getOrder_id());
                intent.putExtra("name", order.getCustomer_name());
                intent.putExtra("mobile", order.getMobile());
                intent.putExtra("payment", order.getMode_of_payment());
                intent.putExtra("houseNo", order.getHouse_flat_no());
                intent.putExtra("street", order.getStreet_name());
                intent.putExtra("address", order.getAddress());
                intent.putExtra("pincode", order.getPincode());
                if (order.getLift_availability().equals("1")) {
                    intent.putExtra("liftAvail","Yes");
                }else {
                    intent.putExtra("liftAvail", "No");
                }
                intent.putExtra("floorNo", order.getTotal_floors());
                intent.putExtra("amount", order.getGrand_total());
                intent.putExtra("product", order.getOrder_products().get(0).getProduct_name());
                intent.putExtra("quantity", order.getOrder_products().get(0).getQuantity());

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, date, total, status;
        CardView deliverItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.order_id);
            date = itemView.findViewById(R.id.date);
            total = itemView.findViewById(R.id.total_amount);
            status = itemView.findViewById(R.id.status);
            deliverItem = itemView.findViewById(R.id.deliverItem);

        }
    }
}

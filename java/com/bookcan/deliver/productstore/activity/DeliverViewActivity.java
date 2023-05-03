package com.bookcan.deliver.productstore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bookcan.deliver.productstore.R;

public class DeliverViewActivity extends AppCompatActivity {
TextView name,mobile,payment,amount,other;
CheckBox amountCollect;
Button delivered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try
//        {
//            this.getSupportActionBar().hide();
//        }
//        catch (NullPointerException e){}
        setContentView(R.layout.activity_deliver_view);

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        payment = findViewById(R.id.payment);
        amount = findViewById(R.id.amount);
        other = findViewById(R.id.otherDetails);
        delivered = findViewById(R.id.deliverBtn);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        mobile.setText(intent.getStringExtra("mobile"));
        payment.setText("Payment Mode : "+intent.getStringExtra("payment"));
        amount.setText("Amount to be received Rs: "+intent.getStringExtra("amount"));
        other.setText("HouseNo : "+intent.getStringExtra("houseNo")
                 +"\n"+"Street : "+intent.getStringExtra("street")
                 +"\n"+"Address : "+intent.getStringExtra("address")
                 +"\n"+"Pincode : "+intent.getStringExtra("pincode")
                +"\n" +"\n"+"Lift Availability : "+intent.getStringExtra("liftAvail")
                +"\n" +"\n"+"Floor No : "+intent.getStringExtra("floorNo")
                +"\n" +"\n"+intent.getStringExtra("product")
                +"\n"+"Quantity : "+intent.getStringExtra("quantity"));



    }
}
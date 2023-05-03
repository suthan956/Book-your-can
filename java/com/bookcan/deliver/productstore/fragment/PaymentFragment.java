package com.bookcan.deliver.productstore.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.activity.BaseActivity;
import com.bookcan.deliver.productstore.activity.CartActivity;
import com.bookcan.deliver.productstore.activity.CheckoutActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {

    RadioGroup paymentGroup;
    FrameLayout cardFrame;
    RadioButton card, cash;
    LinearLayout payll;
    TextView pay;

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_payment, container, false);
        paymentGroup = view.findViewById(R.id.payment_group);
        card = view.findViewById(R.id.card_payment);
        cash = view.findViewById(R.id.cash_on_delivery);
        cardFrame = view.findViewById(R.id.card_frame);
        payll = view.findViewById(R.id.pay_ll);
        pay = view.findViewById(R.id.total_pay);
       // Double amount = ((BaseActivity) getActivity()).getTotalPrice();
        pay.append(""+((CheckoutActivity) getActivity()).finalPrice);
        payll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cash.isChecked()) {
                    //Toast.makeText(requireContext(),"Please Select Cash On Delivery!",Toast.LENGTH_SHORT).show();
                    ((CheckoutActivity) getActivity()).startPayment();
                }
            }
        });
        paymentGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                if (radioButton.getId() == R.id.cash_on_delivery) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                    ft.replace(R.id.content_frame, new ConfirmFragment());
                    ft.commit();
                }
                // Toast.makeText(getContext(),radioButton.getText()+"",Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Payment");
    }
}

package com.bookcan.deliver.productstore.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.adapter.PopularProductAdapter;
import com.bookcan.deliver.productstore.helper.Data;
 
/**
 * A simple {@link Fragment} subclass.
 */
public class PopularProductFragment extends Fragment {
    RecyclerView pRecyclerView;
    Data data;
    private PopularProductAdapter pAdapter;

    public PopularProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        data = new Data();
        pRecyclerView = view.findViewById(R.id.popular_product_rv);
        pAdapter = new PopularProductAdapter(data.getPopularList(), getContext(), "pop");
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getContext());
        pRecyclerView.setLayoutManager(pLayoutManager);
        pRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pRecyclerView.setAdapter(pAdapter);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Popular");
    }
}

package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nolo.R;
import com.example.nolo.viewmodels.PurchasesViewModel;

public class PurchasesFragment extends Fragment {
    private ViewHolder vh;
    private PurchasesViewModel purchasesViewModel;

    private class ViewHolder {

        ListView transitList, deliveredList;
        RelativeLayout backBtn;

        public ViewHolder() {
            transitList = getView().findViewById(R.id.transit_list);
            deliveredList = getView().findViewById(R.id.delivered_list);
            backBtn = getView().findViewById(R.id.back_btn);
        }
    }

    private void initListeners() {
        vh.backBtn.setOnClickListener(v -> {
            System.out.println("clicked");
            getActivity().getSupportFragmentManager().beginTransaction().remove(PurchasesFragment.this).commit();
        });
    }

    private void initAdaptor() {

    }

    public PurchasesFragment() {
        super(R.layout.fragment_purchases);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        purchasesViewModel = new PurchasesViewModel();
        vh = new ViewHolder();
        initListeners();

    }

    @Override
    public void onResume() {
        super.onResume();
        initAdaptor();
    }
}
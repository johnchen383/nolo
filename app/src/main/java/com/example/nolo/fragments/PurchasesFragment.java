package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nolo.R;
import com.example.nolo.adaptors.PurchasableListAdaptor;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.PurchasesViewModel;

public class PurchasesFragment extends Fragment {
    private ViewHolder vh;
    private PurchasesViewModel purchasesViewModel;

    private class ViewHolder {

        ListView transitList, deliveredList;
        LinearLayout transitTitle, deliveredTitle;
        RelativeLayout backBtn;

        public ViewHolder() {
            transitList = getView().findViewById(R.id.transit_list);
            deliveredList = getView().findViewById(R.id.delivered_list);
            transitTitle = getView().findViewById(R.id.transit_title);
            deliveredTitle = getView().findViewById(R.id.delivered_title);
            backBtn = getView().findViewById(R.id.back_btn);
        }
    }

    private void initListeners() {
        vh.backBtn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().remove(PurchasesFragment.this).commit();
        });
    }

    private void initStyling() {
        if (purchasesViewModel.getUserPurchaseHistoryInTransit().isEmpty()) {
            vh.transitTitle.setVisibility(View.GONE);
            vh.transitList.setVisibility(View.GONE);
        }

        if (purchasesViewModel.getUserPurchaseHistoryDelivered().isEmpty()) {
            vh.deliveredTitle.setVisibility(View.GONE);
            vh.deliveredList.setVisibility(View.GONE);
        }
    }

    private void initAdaptors() {
        PurchasableListAdaptor transitPurchasableAdaptor = new PurchasableListAdaptor(getActivity(), this, R.layout.item_list_purchaseable, purchasesViewModel.getUserPurchaseHistoryInTransit(), v->{});
        vh.transitList.setAdapter(transitPurchasableAdaptor);
        ListUtil.setDynamicHeight(vh.transitList);

        PurchasableListAdaptor deliveredPurchasableAdaptor = new PurchasableListAdaptor(getActivity(), this, R.layout.item_list_purchaseable, purchasesViewModel.getUserPurchaseHistoryDelivered(), v->{});
        vh.deliveredList.setAdapter(deliveredPurchasableAdaptor);
        ListUtil.setDynamicHeight(vh.deliveredList);
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
        initStyling();
        initAdaptors();

    }

    @Override
    public void onResume() {
        super.onResume();
        initStyling();
        initAdaptors();
    }
}
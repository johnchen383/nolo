package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nolo.R;
import com.example.nolo.activities.MainActivity;
import com.example.nolo.adaptors.ItemsListVariantAdaptor;
import com.example.nolo.enums.PurchaseStatus;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.IPurchasesViewModel;
import com.example.nolo.viewmodels.PurchasesViewModel;

public class PurchasesFragment extends Fragment {
    private IPurchasesViewModel purchasesViewModel;
    private ViewHolder vh;

    private class ViewHolder {
        ListView transitList, deliveredList;
        LinearLayout transitTitle, deliveredTitle, emptyMsg;
        TextView transitText, deliveredText;
        RelativeLayout backBtn;

        public ViewHolder(View view) {
            transitList = view.findViewById(R.id.transit_list);
            deliveredList = view.findViewById(R.id.delivered_list);
            transitTitle = view.findViewById(R.id.transit_title);
            deliveredTitle = view.findViewById(R.id.delivered_title);
            transitText = view.findViewById(R.id.transit_text);
            deliveredText = view.findViewById(R.id.delivered_text);
            backBtn = view.findViewById(R.id.back_btn);
            emptyMsg = view.findViewById(R.id.empty_msg);
        }
    }

    public PurchasesFragment() {
        super(R.layout.fragment_purchases);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        purchasesViewModel = new PurchasesViewModel();
        vh = new ViewHolder(view);

        // Initialisation
        init();
        ((MainActivity) getActivity()).updateCartBadge();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    /**
     * Initialisation
     */
    private void init() {
        checkPurchasesEmpty();
        initStyling();
        initListeners();
        initAdaptors();
    }

    /**
     * Check if the purchase history is empty, if it is show the empty message
     */
    private void checkPurchasesEmpty() {
        vh.emptyMsg.setVisibility(purchasesViewModel.checkPurchaseHistoryDeliveredEmpty()
                && purchasesViewModel.checkPurchaseHistoryInTransitEmpty()
                ? View.VISIBLE : View.GONE);
    }

    private void initListeners() {
        vh.backBtn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().remove(PurchasesFragment.this).commit();
        });
    }

    private void initStyling() {
        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.navy));

        vh.transitText.setText(PurchaseStatus.inTransit.getFullname().toUpperCase());
        vh.deliveredText.setText(PurchaseStatus.delivered.getFullname().toUpperCase());

        if (purchasesViewModel.checkPurchaseHistoryInTransitEmpty()) {
            vh.transitTitle.setVisibility(View.GONE);
            vh.transitList.setVisibility(View.GONE);
        }

        if (purchasesViewModel.checkPurchaseHistoryDeliveredEmpty()) {
            vh.deliveredTitle.setVisibility(View.GONE);
            vh.deliveredList.setVisibility(View.GONE);
        }
    }

    private void initAdaptors() {
        // Items in Transit
        ItemsListVariantAdaptor transitPurchasableAdaptor
                = new ItemsListVariantAdaptor(getActivity(), this, R.layout.item_list_variant,
                purchasesViewModel.getUserPurchaseHistoryInTransit(), v -> {},
                null, null);
        vh.transitList.setAdapter(transitPurchasableAdaptor);
        ListUtil.setDynamicHeight(vh.transitList);

        // Items in Delivered
        ItemsListVariantAdaptor deliveredPurchasableAdaptor
                = new ItemsListVariantAdaptor(getActivity(), this, R.layout.item_list_variant,
                purchasesViewModel.getUserPurchaseHistoryDelivered(), v -> {},
                null, null);
        vh.deliveredList.setAdapter(deliveredPurchasableAdaptor);
        ListUtil.setDynamicHeight(vh.deliveredList);
    }
}
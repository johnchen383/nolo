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
import com.example.nolo.adaptors.PurchasableListAdaptor;
import com.example.nolo.enums.PurchaseStatus;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.PurchasesViewModel;
import com.example.nolo.viewmodels.WishlistViewModel;

public class WishlistFragment extends Fragment {
    private ViewHolder vh;
    private WishlistViewModel wishlistViewModel;

    private class ViewHolder {

        ListView wishList;
        RelativeLayout backBtn;

        public ViewHolder() {
            wishList = getView().findViewById(R.id.wishlist_list);
            backBtn = getView().findViewById(R.id.back_btn);
        }
    }

    private void initListeners() {
        vh.backBtn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().remove(WishlistFragment.this).commit();
        });
    }

    private void initStyling() {
    }

    private void initAdaptors() {
        PurchasableListAdaptor wishListPurchasableAdaptor = new PurchasableListAdaptor(getActivity(), this, R.layout.item_list_purchaseable, wishlistViewModel.getUserWishlist(), v->{});
        vh.wishList.setAdapter(wishListPurchasableAdaptor);
        ListUtil.setDynamicHeight(vh.wishList);

    }

    public WishlistFragment() {
        super(R.layout.fragment_wishlist);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wishlistViewModel = new WishlistViewModel();
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
package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nolo.R;
import com.example.nolo.adaptors.ItemsListVariantAdaptor;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.WishlistViewModel;

import java.util.List;

public class WishlistFragment extends Fragment {
    private ViewHolder vh;
    private WishlistViewModel wishlistViewModel;

    private class ViewHolder {

        ListView wishList;
        RelativeLayout backBtn;
        LinearLayout emptyMsg;

        public ViewHolder() {
            wishList = getView().findViewById(R.id.wishlist_list);
            backBtn = getView().findViewById(R.id.back_btn);
            emptyMsg = getView().findViewById(R.id.empty_msg);
        }
    }

    private void initListeners() {
        vh.backBtn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().remove(WishlistFragment.this).commit();
        });
    }

    private void initAdaptors() {
        ItemsListVariantAdaptor wishListPurchasableAdaptor = new ItemsListVariantAdaptor(getActivity(), this, R.layout.item_list_variant, null, null, wishlistViewModel.getUserWishlist(), this::updateWishlistItems);
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
        initAdaptors();
        checkWishlistEmpty();

    }

    @Override
    public void onResume() {
        super.onResume();
        initAdaptors();
    }

    private void checkWishlistEmpty(){
        vh.emptyMsg.setVisibility(wishlistViewModel.getUserWishlist().isEmpty() ? View.VISIBLE : View.GONE);
    }

    public void updateWishlistItems(ItemVariant variant) {
        wishlistViewModel.removeWishlistItem(variant);
        initAdaptors();
        checkWishlistEmpty();
    }
}
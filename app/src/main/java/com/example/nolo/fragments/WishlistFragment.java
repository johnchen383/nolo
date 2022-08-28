package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nolo.R;
import com.example.nolo.activities.MainActivity;
import com.example.nolo.adaptors.ItemsListVariantAdaptor;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.IWishlistViewModel;
import com.example.nolo.viewmodels.WishlistViewModel;

public class WishlistFragment extends Fragment {
    private IWishlistViewModel wishlistViewModel;
    private ViewHolder vh;

    private class ViewHolder {
        ListView wishList;
        RelativeLayout backBtn;
        LinearLayout emptyMsg;

        public ViewHolder(View view) {
            wishList = view.findViewById(R.id.wishlist_list);
            backBtn = view.findViewById(R.id.back_btn);
            emptyMsg = view.findViewById(R.id.empty_msg);
        }
    }

    public WishlistFragment() {
        super(R.layout.fragment_wishlist);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wishlistViewModel = new WishlistViewModel();
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
        checkWishlistEmpty();
        initListeners();
        initAdaptors();
        initStyling();
    }

    private void initStyling(){
        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.navy));
    }

    /**
     * Check if the wishlist is empty, if it is show the empty message
     */
    private void checkWishlistEmpty() {
        vh.emptyMsg.setVisibility(wishlistViewModel.checkWishlistEmpty() ? View.VISIBLE : View.GONE);
    }

    private void initListeners() {
        vh.backBtn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().remove(WishlistFragment.this).commit();
        });
    }

    private void initAdaptors() {
        ItemsListVariantAdaptor wishListPurchasableAdaptor
                = new ItemsListVariantAdaptor(getActivity(), this, R.layout.item_list_variant,
                null, null,
                wishlistViewModel.getUserWishlist(), this::removeWishlistItem);
        vh.wishList.setAdapter(wishListPurchasableAdaptor);
        ListUtil.setDynamicHeight(vh.wishList);
    }

    /**
     * Remove the item from wishlist
     *
     * @param variant Item is about to be remove
     */
    public void removeWishlistItem(ItemVariant variant) {
        wishlistViewModel.removeWishlistItem(variant);
        initAdaptors();
        checkWishlistEmpty();
    }
}
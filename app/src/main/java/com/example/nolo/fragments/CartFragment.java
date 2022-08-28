package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.activities.MainActivity;
import com.example.nolo.adaptors.ItemsListVariantAdaptor;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.CartViewModel;
import com.example.nolo.viewmodels.ICartViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Fragment to house the cart 'tab' on the main activity
 * Used for viewing cart items, modifying cart items, and transitioning to purchase
 */
public class CartFragment extends Fragment {
    private ICartViewModel cartViewModel;
    private ViewHolder vh;

    private class ViewHolder {
        TextView totalPrice;
        ListView cartList;
        MaterialButton checkoutBtn;
        LinearLayout emptyMsg;

        public ViewHolder(View view) {
            totalPrice = view.findViewById(R.id.total_price);
            cartList = view.findViewById(R.id.cart_list);
            checkoutBtn = view.findViewById(R.id.checkout_btn);
            emptyMsg = view.findViewById(R.id.empty_msg);
        }
    }

    public CartFragment() {
        super(R.layout.fragment_cart);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

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
        checkCartEmpty();
        updatePrice();
        initListeners();
        initAdaptors();
    }

    /**
     * Check if the cart is empty, if it is show the empty message
     */
    private void checkCartEmpty() {
        vh.emptyMsg.setVisibility(cartViewModel.checkCartEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updatePrice() {
        double sum = 0;

        for (IPurchasable purchase : cartViewModel.getUserCart()) {
            sum += purchase.getQuantity() * purchase.getItemVariant().getNumericalPrice();
        }

        String price = String.format("$%.2f NZD", sum);
        vh.totalPrice.setText(price);
    }

    private void initListeners() {
        vh.checkoutBtn.setOnClickListener(v -> {
            cartViewModel.moveCartToPurchaseHistory();
            Toast.makeText(getContext(), "Purchase made!", Toast.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).updateCartBadge();
        });
    }

    private void initAdaptors() {
        ItemsListVariantAdaptor cartPurchasableAdaptor =
                new ItemsListVariantAdaptor(getActivity(), this, R.layout.item_list_variant,
                        cartViewModel.getUserCart(), this::updateCartItems,
                        null, null);
        vh.cartList.setAdapter(cartPurchasableAdaptor);
        ListUtil.setDynamicHeight(vh.cartList);
    }

    /**
     * Update the cart when some actions are done to the cart
     *
     * @param items New items list
     */
    private void updateCartItems(List<Purchasable> items) {
        cartViewModel.updateCartItem(items);
        updatePrice();
        initAdaptors();
        checkCartEmpty();

        ((MainActivity) getActivity()).updateCartBadge();
    }
}
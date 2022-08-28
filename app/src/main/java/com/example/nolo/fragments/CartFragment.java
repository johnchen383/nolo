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
import com.example.nolo.adaptors.PurchasableListAdaptor;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.CartViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Fragment to house the cart 'tab' on the main activity
 * Used for viewing cart items, modifying cart items, and transitioning to purchase
 */
public class CartFragment extends Fragment {
    private ViewHolder vh;
    private CartViewModel cartViewModel;

    private class ViewHolder {
        TextView totalPrice;
        ListView cartList;
        MaterialButton checkoutBtn;
        LinearLayout emptyMsg;

        public ViewHolder(View view){
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

        ((MainActivity) getActivity()).updateCartBadge();

        updatePrice();
        initAdaptor();

        checkCartEmpty();

        vh.checkoutBtn.setOnClickListener(v -> {
            cartViewModel.moveCartToPurchaseHistory();
            Toast.makeText(getContext(), "Purchase made!", Toast.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).updateCartBadge();
        });
    }

    private void checkCartEmpty(){
        vh.emptyMsg.setVisibility(cartViewModel.getUserCart().isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateCartItems(List<Purchasable> items){
        cartViewModel.updateCartItem(items);
        updatePrice();
        initAdaptor();
        checkCartEmpty();

        ((MainActivity) getActivity()).updateCartBadge();
    }

    @Override
    public void onResume() {
        super.onResume();
        initAdaptor();
    }

    private void initAdaptor(){
//        cartItems.add(null);
//        cartItems.add(null);
//        cartItems.add(null);
//        cartItems.add(null);
//        cartItems.add(null);

        PurchasableListAdaptor cartPurchasableAdaptor = new PurchasableListAdaptor(getActivity(), this, R.layout.item_list_purchaseable, cartViewModel.getUserCart(), this::updateCartItems);
        vh.cartList.setAdapter(cartPurchasableAdaptor);

        ListUtil.setDynamicHeight(vh.cartList);
    }

    private void updatePrice(){
        double sum = 0;

        for (IPurchasable purchase : cartViewModel.getUserCart()){
            sum += purchase.getQuantity() * purchase.getItemVariant().getNumericalPrice();
        }

        String price = String.format("$%.2f NZD", sum);
        vh.totalPrice.setText(price);
    }
}
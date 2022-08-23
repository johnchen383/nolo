package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.adaptors.CartPurchasableAdaptor;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.user.IUser;
import com.example.nolo.interactors.user.GetCurrentUserUseCase;
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
    private IUser user;
    private List<Purchasable> cartItems;

    private class ViewHolder {
        TextView totalPrice;
        ListView cartList;
        MaterialButton checkoutBtn;

        public ViewHolder(){
            totalPrice = getView().findViewById(R.id.total_price);
            cartList = getView().findViewById(R.id.cart_list);
            checkoutBtn = getView().findViewById(R.id.checkout_btn);
        }
    }

    public CartFragment() {
        super(R.layout.fragment_cart);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        vh = new ViewHolder();
        user = GetCurrentUserUseCase.getCurrentUser();
        cartItems = user.getCart();

        updatePrice();
        initAdaptor();
    }

    private void updateCartItems(List<Purchasable> items){
        cartItems = items;
        updatePrice();
        initAdaptor();
    }

    private void initAdaptor(){
//        cartItems.add(null);
//        cartItems.add(null);
//        cartItems.add(null);
//        cartItems.add(null);
//        cartItems.add(null);

        CartPurchasableAdaptor categoriesAdaptor = new CartPurchasableAdaptor(getActivity(), R.layout.item_list_cart, cartItems, (items) -> updateCartItems(items));
        vh.cartList.setAdapter(categoriesAdaptor);

        ListUtil.setDynamicHeight(vh.cartList);
    }

    private void updatePrice(){
        double sum = 0;

        for (IPurchasable purchase : cartItems){
            sum += purchase.getQuantity() * purchase.getItemVariant().getNumericalPrice();
        }

        String price = String.format("$%.2f NZD", sum);
        vh.totalPrice.setText(price);
    }
}
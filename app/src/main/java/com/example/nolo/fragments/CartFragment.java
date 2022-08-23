package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.viewmodels.CartViewModel;

/**
 * Fragment to house the cart 'tab' on the main activity
 * Used for viewing cart items, modifying cart items, and transitioning to purchase
 */
public class CartFragment extends Fragment {
    private ViewHolder vh;
    private CartViewModel cartViewModel;

    private class ViewHolder {
        TextView totalPrice;

        public ViewHolder(){
            totalPrice = getView().findViewById(R.id.total_price);
        }
    }

    public CartFragment() {
        super(R.layout.fragment_cart);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);
    }
}
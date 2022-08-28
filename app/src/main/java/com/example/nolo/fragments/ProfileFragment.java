package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nolo.R;
import com.example.nolo.activities.MainActivity;
import com.example.nolo.util.FragmentUtil;

/**
 * Fragment to house the profile 'tab' on the main activity
 * Used for viewing profile information, modifying profile information and performing profile actions (e.g., logout)
 */
public class ProfileFragment extends Fragment {
    private ViewHolder vh;

    private class ViewHolder {
        TextView purchasesBtn, wishlistBtn, accountBtn;

        public ViewHolder(View view) {
            purchasesBtn = view.findViewById(R.id.purchases_btn);
            wishlistBtn = view.findViewById(R.id.wishlist_btn);
            accountBtn = view.findViewById(R.id.account_btn);
        }
    }

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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
        initListeners();
        initStyling();
    }

    private void initStyling(){
        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.navy));
    }

    private void initListeners() {
        vh.accountBtn.setOnClickListener(v -> {
            FragmentUtil.addFragment(getActivity(), R.id.profile_fragment, AccountFragment.class, "PROFILE_ADDITION");
        });

        vh.wishlistBtn.setOnClickListener(v -> {
            FragmentUtil.addFragment(getActivity(), R.id.profile_fragment, WishlistFragment.class, "PROFILE_ADDITION");
        });

        vh.purchasesBtn.setOnClickListener(v -> {
            FragmentUtil.addFragment(getActivity(), R.id.profile_fragment, PurchasesFragment.class, "PROFILE_ADDITION");
        });
    }
}
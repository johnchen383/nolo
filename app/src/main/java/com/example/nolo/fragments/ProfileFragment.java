package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.util.FragmentUtil;
import com.example.nolo.activities.LogInActivity;
import com.example.nolo.activities.MainActivity;
import com.example.nolo.util.Animation;
import com.example.nolo.viewmodels.ChangePasswordViewModel;
import com.example.nolo.viewmodels.ProfileViewModel;

/**
 * Fragment to house the profile 'tab' on the main activity
 * Used for viewing profile information, modifying profile information and performing profile actions (e.g., logout)
 */
public class ProfileFragment extends Fragment {
    private ViewHolder vh;
    private ProfileViewModel profileViewModel;

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
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        vh = new ViewHolder(view);

        ((MainActivity) getActivity()).updateCartBadge();
        initListeners();
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
package com.example.nolo.fragments;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.util.Animation;
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

        public ViewHolder() {
            purchasesBtn = getView().findViewById(R.id.purchases_btn);
            wishlistBtn = getView().findViewById(R.id.wishlist_btn);
            accountBtn = getView().findViewById(R.id.account_btn);
        }
    }

    private void initListeners() {
        vh.accountBtn.setOnClickListener(v -> {
            replaceFragment(AccountFragment.class);
        });
    }

    private void replaceFragment(Class<? extends Fragment> fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.profile_fragment, fragment, null, "PROFILE_ADDITION");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        vh = new ViewHolder();
        initListeners();
    }
}
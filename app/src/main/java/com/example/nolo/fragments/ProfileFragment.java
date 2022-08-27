package com.example.nolo.fragments;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.activities.LogInActivity;
import com.example.nolo.util.Animation;
import com.example.nolo.viewmodels.ChangePasswordViewModel;
import com.example.nolo.viewmodels.ProfileViewModel;
import com.google.android.material.button.MaterialButton;

/**
 * Fragment to house the profile 'tab' on the main activity
 * Used for viewing profile information, modifying profile information and performing profile actions (e.g., logout)
 */
public class ProfileFragment extends Fragment {
    private ViewHolder vh;
    private ProfileViewModel profileViewModel;

    private class ViewHolder {
        TextView emailText;
        MaterialButton signoutBtn;
        RelativeLayout changePasswordBtn, purchasesBtn;

        public ViewHolder() {
            emailText = getView().findViewById(R.id.email_text);
            changePasswordBtn = getView().findViewById(R.id.change_password_btn);
            purchasesBtn = getView().findViewById(R.id.purchases_btn);
            signoutBtn = getView().findViewById(R.id.sign_out_btn);
        }
    }

    private void initListeners() {
        vh.changePasswordBtn.setOnClickListener(v -> {
            replaceFragment(ChangePasswordFragment.class);
        });

        vh.purchasesBtn.setOnClickListener(v -> {
            replaceFragment(PurchasesFragment.class);
        });

        vh.signoutBtn.setOnClickListener(v -> {
            profileViewModel.logOut();
            startActivity(new Intent(getActivity(), LogInActivity.class), Animation.Fade(getActivity()).toBundle());
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
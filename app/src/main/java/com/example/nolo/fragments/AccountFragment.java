package com.example.nolo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nolo.R;
import com.example.nolo.activities.LogInActivity;
import com.example.nolo.activities.MainActivity;
import com.example.nolo.util.Animation;
import com.example.nolo.util.FragmentUtil;
import com.example.nolo.viewmodels.AccountViewModel;
import com.example.nolo.viewmodels.IAccountViewModel;
import com.google.android.material.button.MaterialButton;

public class AccountFragment extends Fragment {
    private IAccountViewModel accountViewModel;
    private ViewHolder vh;

    private class ViewHolder {
        RelativeLayout changePasswordBtn, backBtn;
        TextView emailText;
        MaterialButton signoutBtn;

        public ViewHolder(View view) {
            changePasswordBtn = view.findViewById(R.id.change_password_btn);
            backBtn = view.findViewById(R.id.back_btn);
            emailText = view.findViewById(R.id.email_text);
            signoutBtn = view.findViewById(R.id.sign_out_btn);
        }
    }

    public AccountFragment() {
        super(R.layout.fragment_account);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountViewModel = new AccountViewModel();
        vh = new ViewHolder(view);

        // Initialisation
        init();
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
        ((MainActivity) getActivity()).updateCartBadge();

        initStyling();
        initListeners();
    }

    private void initStyling() {
        vh.emailText.setText(accountViewModel.getCurrentUserEmail());
    }

    private void initListeners() {
        vh.changePasswordBtn.setOnClickListener(v -> {
            FragmentUtil.addFragment(getActivity(), R.id.account_fragment, ChangePasswordFragment.class, "PROFILE_ADDITION");
        });

        vh.backBtn.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().remove(AccountFragment.this).commit();
        });

        vh.signoutBtn.setOnClickListener(v -> {
            accountViewModel.logOut();
            startActivity(new Intent(getActivity(), LogInActivity.class), Animation.Fade(getActivity()).toBundle());
        });
    }
}
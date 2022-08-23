package com.example.nolo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.nolo.R;
import com.example.nolo.activities.BaseActivity;
import com.example.nolo.activities.LogInActivity;
import com.example.nolo.util.Animation;
import com.example.nolo.viewmodels.AccountViewModel;
import com.google.android.material.button.MaterialButton;

public class AccountFragment extends Fragment {
    private ViewHolder vh;
    private AccountViewModel accountViewModel;

    private class ViewHolder {

        RelativeLayout backBtn;
        TextView emailText;
        MaterialButton signoutBtn;

        public ViewHolder() {
            backBtn = getView().findViewById(R.id.back_btn);
            emailText = getView().findViewById(R.id.email_text);
            signoutBtn = getView().findViewById(R.id.sign_out_btn);
        }
    }

    private void initStyling() {
        vh.emailText.setText(accountViewModel.getCurrentUserEmail());
    }

    private void initListeners() {
        vh.backBtn.setOnClickListener(v -> {
            System.out.println("clicked");
            getActivity().getSupportFragmentManager().beginTransaction().remove(AccountFragment.this).commit();
        });

        vh.signoutBtn.setOnClickListener(v -> {
            accountViewModel.logOut();
            startActivity(new Intent(getActivity(), LogInActivity.class), Animation.Fade(getActivity()).toBundle());
        });
    }

    public AccountFragment() {
        super(R.layout.fragment_account);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountViewModel = new AccountViewModel();
        vh = new ViewHolder();
        initStyling();
        initListeners();

    }
}

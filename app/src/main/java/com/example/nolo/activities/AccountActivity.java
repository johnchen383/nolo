package com.example.nolo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nolo.R;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.util.Animation;
import com.example.nolo.viewmodels.AccountViewModel;
import com.google.android.material.button.MaterialButton;

public class AccountActivity extends BaseActivity {
    private ViewHolder vh;
    private AccountViewModel accountViewModel;

    private class ViewHolder {

        RelativeLayout backBtn;
        TextView emailText;
        MaterialButton signoutBtn;

        public ViewHolder() {
            backBtn = findViewById(R.id.back_btn);
            emailText = findViewById(R.id.email_text);
            signoutBtn = findViewById(R.id.sign_out_btn);
        }
    }

    private void initStyling() {
        vh.emailText.setText(accountViewModel.getCurrentUserEmail());
    }

    private void initListeners() {
        vh.backBtn.setOnClickListener(v -> {
            super.onBackPressed();
            this.finish();
        });

        vh.signoutBtn.setOnClickListener(v -> {
            accountViewModel.logOut();
            startActivity(new Intent(this, LogInActivity.class), Animation.Fade(this).toBundle());
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        accountViewModel = new AccountViewModel();
        vh = new ViewHolder();

        initStyling();
        initListeners();

    }
}

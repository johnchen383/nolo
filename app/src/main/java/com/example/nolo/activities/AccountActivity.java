package com.example.nolo.activities;

import android.os.Bundle;

import com.example.nolo.R;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.viewmodels.DetailsViewModel;

public class AccountActivity extends BaseActivity {
    private ViewHolder vh;

    private class ViewHolder {

        public ViewHolder() {

        }
    }

    private void initStyling() {

    }

    private void initListeners() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IItemVariant itemVariant = (IItemVariant) getIntent().getSerializableExtra(getString(R.string.extra_item_variant));
        setContentView(R.layout.activity_details);
        vh = new ViewHolder();

        initStyling();
        initListeners();

    }
}

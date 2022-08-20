package com.example.nolo.activities;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.example.nolo.R;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.viewmodels.DetailsViewModel;
import com.example.nolo.viewmodels.LogInViewModel;

public class DetailsActivity extends BaseActivity {
    private DetailsViewModel detailsViewModel;

    private ViewHolder vh;

    private class ViewHolder {

        public ViewHolder() {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemVariant item = new ItemVariant();
        detailsViewModel =  new DetailsViewModel(item);
        setContentView(R.layout.activity_details);
        vh = new ViewHolder();

    }
}

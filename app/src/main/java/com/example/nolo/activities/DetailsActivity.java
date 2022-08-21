package com.example.nolo.activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.nolo.R;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.util.Display;
import com.example.nolo.viewmodels.DetailsViewModel;

public class DetailsActivity extends BaseActivity {
    private DetailsViewModel detailsViewModel;

    private ViewHolder vh;

    private class ViewHolder {
        LinearLayout transparentContainer, detailsContainer;
        TextView itemTitle;

        public ViewHolder() {
            transparentContainer = findViewById(R.id.transparent_container);
            detailsContainer = findViewById(R.id.details_container);
            itemTitle = findViewById(R.id.item_title);
        }
    }

    private void initStyling() {
        vh.transparentContainer.setMinimumHeight((int) (0.45 * (Display.getScreenHeight(vh.transparentContainer))));
        vh.detailsContainer.setMinimumHeight(Display.getScreenHeight(vh.detailsContainer));
        vh.itemTitle.setText(detailsViewModel.getItemName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IItemVariant itemVariant = (IItemVariant) getIntent().getSerializableExtra(getString(R.string.extra_item_variant));
        detailsViewModel = new DetailsViewModel(itemVariant);
        setContentView(R.layout.activity_details);
        vh = new ViewHolder();

        initStyling();

    }
}

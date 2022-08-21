package com.example.nolo.activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.adaptors.DetailsColorAdaptor;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.util.Display;
import com.example.nolo.viewmodels.DetailsViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DetailsActivity extends BaseActivity {
    private DetailsViewModel detailsViewModel;

    private ViewHolder vh;

    private class ViewHolder {
        LinearLayout transparentContainer, detailsContainer;
        TextView itemTitle, colourTitle;
        RecyclerView coloursList;
        MaterialButton addCartBtn;

        public ViewHolder() {
            transparentContainer = findViewById(R.id.transparent_container);
            detailsContainer = findViewById(R.id.details_container);
            itemTitle = findViewById(R.id.item_title);
            colourTitle = findViewById(R.id.colour_title);
            coloursList = findViewById(R.id.colours_list);
            addCartBtn = findViewById(R.id.add_cart_btn);
        }
    }

    private void initAdaptors() {
        /**
         * COLOURS ADAPTOR
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.coloursList.setLayoutManager(layoutManager);

        List<Colour> colours =  detailsViewModel.getItemColours();
        DetailsColorAdaptor categoriesAdaptor = new DetailsColorAdaptor(this, colours, detailsViewModel.getItemVariant(), v -> updateAdaptor(v));
        vh.coloursList.setAdapter(categoriesAdaptor);
    }

    private void initStyling() {
        vh.transparentContainer.setMinimumHeight((int) (0.45 * (Display.getScreenHeight(vh.transparentContainer))));
        vh.detailsContainer.setMinimumHeight(Display.getScreenHeight(vh.detailsContainer));
        vh.itemTitle.setText(detailsViewModel.getItemName());
        setDynamicStyling();
    }

    private void setDynamicStyling() {
        vh.colourTitle.setText(capitaliseFirst(detailsViewModel.getVariantColour().getName()));
    }

    private void initListeners() {

    }

    private void updateAdaptor(IItemVariant itemVariant) {
        detailsViewModel.setItemVariant(itemVariant);
        initAdaptors();
        setDynamicStyling();
    }

    private String capitaliseFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IItemVariant itemVariant = (IItemVariant) getIntent().getSerializableExtra(getString(R.string.extra_item_variant));
        detailsViewModel = new DetailsViewModel(itemVariant);
        setContentView(R.layout.activity_details);
        vh = new ViewHolder();

        initAdaptors();
        initStyling();
        initListeners();

    }
}

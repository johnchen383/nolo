package com.example.nolo.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.adaptors.DetailsColorAdaptor;
import com.example.nolo.adaptors.HomeCategoryAdaptor;
import com.example.nolo.adaptors.HomeFeaturedItemsAdaptor;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.interactors.category.GetCategoriesUseCase;
import com.example.nolo.util.Display;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.DetailsViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DetailsActivity extends BaseActivity {
    private DetailsViewModel detailsViewModel;

    private ViewHolder vh;

    private class ViewHolder {
        LinearLayout transparentContainer, detailsContainer;
        TextView itemTitle;
        RecyclerView coloursList;
        MaterialButton addCartBtn;

        public ViewHolder() {
            transparentContainer = findViewById(R.id.transparent_container);
            detailsContainer = findViewById(R.id.details_container);
            itemTitle = findViewById(R.id.item_title);
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

        List<Colour> colours =  detailsViewModel.getColours();
        DetailsColorAdaptor categoriesAdaptor = new DetailsColorAdaptor(this, colours);
        vh.coloursList.setAdapter(categoriesAdaptor);
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

        initAdaptors();
        initStyling();

    }
}

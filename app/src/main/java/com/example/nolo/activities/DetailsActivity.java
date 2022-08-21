package com.example.nolo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nolo.R;
import com.example.nolo.adaptors.DetailsColorAdaptor;
import com.example.nolo.adaptors.DetailsCustomisationAdaptor;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.util.Display;
import com.example.nolo.viewmodels.DetailsViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DetailsActivity extends BaseActivity {
    private DetailsViewModel detailsViewModel;

    private ViewHolder vh;

    private class ViewHolder {
        LinearLayout transparentContainer, detailsContainer, ramContainer, storageContainer;
        TextView itemTitle, colourTitle, quantityText, decrementBtn, incrementBtn;
        RecyclerView coloursList, ramList, storageList;
        MaterialButton addCartBtn;

        public ViewHolder() {
            transparentContainer = findViewById(R.id.transparent_container);
            detailsContainer = findViewById(R.id.details_container);
            ramContainer = findViewById(R.id.ram_container);
            storageContainer = findViewById(R.id.storage_container);
            itemTitle = findViewById(R.id.item_title);
            colourTitle = findViewById(R.id.colour_title);
            coloursList = findViewById(R.id.colours_list);
            ramList = findViewById(R.id.ram_list);
            storageList = findViewById(R.id.storage_list);
            addCartBtn = findViewById(R.id.add_cart_btn);
        }
    }

    private void initAdaptors() {
        /**
         * COLOURS ADAPTOR
         */
        LinearLayoutManager coloursLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.coloursList.setLayoutManager(coloursLayoutManager);

        List<Colour> colours =  detailsViewModel.getItemColours();
        DetailsColorAdaptor coloursAdaptor = new DetailsColorAdaptor(this, colours, detailsViewModel.getItemVariant(), v -> updateAdaptor(v));
        vh.coloursList.setAdapter(coloursAdaptor);

        /**
         * CUSTOMISATION ADAPTOR
         */
        LinearLayoutManager storageLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.storageList.setLayoutManager(storageLayoutManager);

        List<SpecsOption> storageOptions = detailsViewModel.getStorageOptions();
        System.out.println(storageOptions);
        if (storageOptions != null) {
            DetailsCustomisationAdaptor storageAdaptor = new DetailsCustomisationAdaptor(this, storageOptions, detailsViewModel.getItemVariant(), v -> updateAdaptor(v));
            vh.storageList.setAdapter(storageAdaptor);
        }

        LinearLayoutManager ramLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.ramList.setLayoutManager(ramLayoutManager);
        List<SpecsOption> ramOptions = detailsViewModel.getRamOptions();
        if (ramOptions != null) {
            DetailsCustomisationAdaptor ramAdaptor = new DetailsCustomisationAdaptor(this, ramOptions, detailsViewModel.getItemVariant(), v -> updateAdaptor(v));
            vh.storageList.setAdapter(ramAdaptor);
        }
    }

    private void initStyling() {
        vh.transparentContainer.setMinimumHeight((int) (0.45 * (Display.getScreenHeight(vh.transparentContainer))));
        vh.detailsContainer.setMinimumHeight(Display.getScreenHeight(vh.detailsContainer));
        vh.itemTitle.setText(detailsViewModel.getItemName());

        switch (detailsViewModel.getItemCategory()) {
            case phones:
                vh.ramContainer.setVisibility(View.INVISIBLE);
            case accessories:
                vh.ramContainer.setVisibility(View.INVISIBLE);
                vh.storageContainer.setVisibility(View.INVISIBLE);
        }

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

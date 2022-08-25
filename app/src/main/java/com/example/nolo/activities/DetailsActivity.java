package com.example.nolo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.nolo.R;
import com.example.nolo.adaptors.CarouselPagerAdaptor;
import com.example.nolo.adaptors.DetailsColorAdaptor;
import com.example.nolo.adaptors.DetailsCustomisationAdaptor;
import com.example.nolo.adaptors.DetailsSpecsAdaptor;
import com.example.nolo.adaptors.ItemsCompactAdaptor;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.specs.LaptopSpecs;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.entities.user.IUser;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.SpecsOptionType;
import com.example.nolo.enums.SpecsType;
import com.example.nolo.interactors.user.GetCurrentUserUseCase;
import com.example.nolo.util.Display;
import com.example.nolo.util.ListUtil;
import com.example.nolo.viewmodels.DetailsViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DetailsActivity extends FragmentActivity {
    private DetailsViewModel detailsViewModel;

    private ViewHolder vh;
    private int imgIndex;
    private int maxIndex;
    private float historicX;
    private double heightFactor;
    private Colour displayedColour;

    private class ViewHolder {
        HorizontalScrollView transparentContainer;
        LinearLayout detailsContainer, recContainer, ramContainer, storageContainer, specs;
        TextView itemTitle, summaryText, colourTitle, quantityText, storeName, priceText;
        ListView specsList;

        RelativeLayout decrementBtn, incrementBtn;
        RecyclerView coloursList, ramList, storageList, recItemsList;
        ImageView closeBtn, storesBtn;
        MaterialButton addCartBtn;
        ViewPager2 carousel;
        ScrollView scrollContainer;

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
            quantityText = findViewById(R.id.quantity_text);
            decrementBtn = findViewById(R.id.decrement_btn);
            incrementBtn = findViewById(R.id.increment_btn);
            addCartBtn = findViewById(R.id.add_cart_btn);
            storeName = findViewById(R.id.store_name);
            priceText = findViewById(R.id.price_text);
            storesBtn = findViewById(R.id.store_btn);
            closeBtn = findViewById(R.id.close_btn);

            specs = findViewById(R.id.specs);
            specsList = findViewById(R.id.specs_list);

            carousel = findViewById(R.id.carousel);
            scrollContainer = findViewById(R.id.scrollContainer);
            recItemsList = findViewById(R.id.rec_items_list);
            recContainer = findViewById(R.id.rec_container);
            summaryText = findViewById(R.id.summary_text);
        }
    }

    private void initCarouselAdaptor() {
        /**
         * CAROUSEL
         */
        List<String> uris = detailsViewModel.getImageUrisByColour();
        maxIndex = uris.size() - 1;
        CarouselPagerAdaptor pagerAdapter = new CarouselPagerAdaptor(this, uris);
        vh.carousel.setAdapter(pagerAdapter);
        vh.carousel.setCurrentItem(imgIndex, false);
        displayedColour = detailsViewModel.getVariantColour();
    }

    private void initAdaptors() {
        /**
         * COLOURS ADAPTOR
         */
        LinearLayoutManager coloursLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.coloursList.setLayoutManager(coloursLayoutManager);

        List<Colour> colours = detailsViewModel.getItemColours();
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
            DetailsCustomisationAdaptor storageAdaptor = new DetailsCustomisationAdaptor(this, storageOptions, SpecsOptionType.storage, detailsViewModel.getItemVariant(), v -> updateAdaptor(v));
            vh.storageList.setAdapter(storageAdaptor);
        }

        LinearLayoutManager ramLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.ramList.setLayoutManager(ramLayoutManager);
        List<SpecsOption> ramOptions = detailsViewModel.getRamOptions();
        if (ramOptions != null) {
            DetailsCustomisationAdaptor ramAdaptor = new DetailsCustomisationAdaptor(this, ramOptions, SpecsOptionType.ram, detailsViewModel.getItemVariant(), v -> updateAdaptor(v));
            vh.ramList.setAdapter(ramAdaptor);
        }

        /**
         * REC
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.recItemsList.setLayoutManager(layoutManager);
        ItemsCompactAdaptor featuredItemsAdaptor = new ItemsCompactAdaptor(this, detailsViewModel.getRecItemVariants(), 0.43);
        vh.recItemsList.setAdapter(featuredItemsAdaptor);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (DetailsViewModel.itemVariantFromMap != null) {
//            System.out.println("SETTING FROM RESUME");
            detailsViewModel = new DetailsViewModel(DetailsViewModel.itemVariantFromMap);
            init();
            DetailsViewModel.itemVariantFromMap = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        IUser usr = GetCurrentUserUseCase.getCurrentUser();

        if (usr != null) {
            usr.addViewHistory(detailsViewModel.getItemVariant());
        }
    }

    private void initStyling() {
        heightFactor = 0.45;

        vh.detailsContainer.setMinimumHeight(Display.getScreenHeight(vh.detailsContainer));
        vh.itemTitle.setText(detailsViewModel.getItemName());
        vh.storeName.setText(detailsViewModel.getStoreBranchName());

        switch (detailsViewModel.getItemCategory()) {
            case laptops:
                initSpecsStyling(CategoryType.laptops);
                break;
            case phones:
                heightFactor = 0.55;
                vh.ramContainer.setVisibility(View.GONE);
                initSpecsStyling(CategoryType.phones);
                break;
            case accessories:
                heightFactor = 0.58;
                vh.ramContainer.setVisibility(View.GONE);
                vh.storageContainer.setVisibility(View.GONE);
                initSpecsStyling(CategoryType.accessories);
                break;
        }

        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) vh.transparentContainer.getLayoutParams();
        params2.height = (int) (heightFactor * (Display.getScreenHeight(vh.transparentContainer)));
        vh.transparentContainer.setLayoutParams(params2);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vh.carousel.getLayoutParams();
        params.height = (int) (heightFactor * (Display.getScreenHeight(vh.transparentContainer)));
        vh.carousel.setLayoutParams(params);

        setDynamicStyling();
    }

    private void setDynamicStyling() {
        vh.colourTitle.setText(capitaliseFirst(detailsViewModel.getVariantColour().getName()));
        System.out.println("price: " + detailsViewModel.getItemVariant().getDisplayPrice());
        vh.priceText.setText(detailsViewModel.getItemVariant().getDisplayPrice() + " NZD");
    }

    private void initListeners() {
        vh.decrementBtn.setOnClickListener(v -> {
            detailsViewModel.incrementOrDecrementQuantity(false);
            vh.quantityText.setText(String.valueOf(detailsViewModel.getQuantity()));
        });

        vh.incrementBtn.setOnClickListener(v -> {
            detailsViewModel.incrementOrDecrementQuantity(true);
            vh.quantityText.setText(String.valueOf(detailsViewModel.getQuantity()));
        });

        vh.storesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra(getString(R.string.extra_item_variant), (ItemVariant) detailsViewModel.getItemVariant());
            startActivity(intent);
        });

        vh.addCartBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
            detailsViewModel.addCart();
        });

        vh.closeBtn.setOnClickListener(v -> {
            IUser usr = GetCurrentUserUseCase.getCurrentUser();

            if (usr != null) {
                usr.addViewHistory(detailsViewModel.getItemVariant());
            }

            super.onBackPressed();
            this.finish();
        });

        vh.transparentContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                System.out.println("SWIPE: " + motionEvent.getAction());
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        historicX = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float currentX = motionEvent.getX();

                        if (currentX < historicX) {
                            imgIndex++;
                            if (imgIndex > maxIndex) imgIndex = maxIndex;
                            vh.carousel.setCurrentItem(imgIndex);
                            System.out.println("SWIPE: " + imgIndex);
                        } else if (currentX > historicX) {
                            imgIndex--;
                            if (imgIndex < 0) imgIndex = 0;
                            vh.carousel.setCurrentItem(imgIndex);
                            System.out.println("SWIPE: " + imgIndex);
                        }

                }
                return false;
            }
        });

        vh.scrollContainer.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (vh.scrollContainer.getScrollY() > ((Display.getScreenHeight(vh.scrollContainer) * heightFactor) - 100)) {
                    vh.closeBtn.setVisibility(View.INVISIBLE);
                } else {
                    vh.closeBtn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateAdaptor(IItemVariant itemVariant) {
        detailsViewModel.setItemVariant(itemVariant);
        initAdaptors();
        setDynamicStyling();
        initSpecsStyling(detailsViewModel.getItemCategory());

        if (!displayedColour.equals(itemVariant.getColour())) {
            //colour changed
            initCarouselAdaptor();
        }
    }

    private String capitaliseFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        vh = new ViewHolder();

        IItemVariant itemVariant = (IItemVariant) getIntent().getSerializableExtra(getString(R.string.extra_item_variant));
        detailsViewModel = new DetailsViewModel(itemVariant);
//        System.out.println("SETTING FROM CREATE");
        init();
    }

    private void init() {
        imgIndex = 0;
        initStyling();
        initAdaptors();
        initCarouselAdaptor();
        initListeners();
    }

    private void initSpecsStyling(CategoryType category) {
        if (category.equals(CategoryType.accessories)) {
            vh.specs.setVisibility(View.GONE);
            vh.recContainer.setVisibility(View.GONE);
            vh.summaryText.setText(detailsViewModel.getItemSpecs().getFixedSpecs().get(SpecsType.summary));
            return;
        }

        vh.summaryText.setVisibility(View.GONE);

        DetailsSpecsAdaptor detailsSpecsAdaptor =
                new DetailsSpecsAdaptor(this, R.layout.item_details_description,
                        LaptopSpecs.FIXED_SPECS, detailsViewModel.getItemSpecs().getFixedSpecs());
        vh.specsList.setAdapter(detailsSpecsAdaptor);
        ListUtil.setDynamicHeight(vh.specsList);
    }
}

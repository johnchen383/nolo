package com.example.nolo.activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.nolo.R;
import com.example.nolo.adaptors.CarouselPagerAdaptor;
import com.example.nolo.adaptors.DetailsColorAdaptor;
import com.example.nolo.adaptors.DetailsCustomisationAdaptor;
import com.example.nolo.adaptors.DetailsSpecsAdaptor;
import com.example.nolo.adaptors.ItemsCompactAdaptor;
import com.example.nolo.entities.item.Laptop;
import com.example.nolo.entities.item.Phone;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.SpecsOptionType;
import com.example.nolo.enums.SpecsType;
import com.example.nolo.util.Display;
import com.example.nolo.util.ListUtil;
import com.example.nolo.util.ResponsiveView;
import com.example.nolo.viewmodels.DetailsViewModel;
import com.example.nolo.viewmodels.IDetailsViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class DetailsActivity extends BaseActivity {
    private IDetailsViewModel detailsViewModel;

    private static final int ANIMATION_INTERVAL = 250;
    private ViewHolder vh;
    private int imgIndex;
    private int maxIndex;
    private float historicX;
    private float historicY;
    private double heightFactor;
    private Colour displayedColour;
    private boolean isExpanded;

    private class ViewHolder {
        HorizontalScrollView transparentContainer;
        LinearLayout detailsContainer, recContainer, ramContainer, storageContainer, specs;
        TextView itemTitle, summaryText, colourTitle, quantityText, storeName, priceText;
        ListView specsList;
        TabLayout dots;

        RelativeLayout decrementBtn, incrementBtn, carouselContainer;
        RecyclerView coloursList, ramList, storageList, recItemsList;
        ImageView closeBtn, storesBtn, heartHollowBtn, heartFilledBtn;
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
            heartHollowBtn = findViewById(R.id.heart_hollow_btn);
            heartFilledBtn = findViewById(R.id.heart_filled_btn);

            specs = findViewById(R.id.specs);
            specsList = findViewById(R.id.specs_list);

            carousel = findViewById(R.id.carousel);
            scrollContainer = findViewById(R.id.scrollContainer);
            recItemsList = findViewById(R.id.rec_items_list);
            recContainer = findViewById(R.id.rec_container);
            summaryText = findViewById(R.id.summary_text);
            dots = findViewById(R.id.dots);
            carouselContainer = findViewById(R.id.carousel_container);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        vh = new ViewHolder();
        isExpanded = false;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        IItemVariant itemVariant = (IItemVariant) getIntent().getSerializableExtra(getString(R.string.extra_item_variant));
        detailsViewModel = new DetailsViewModel(itemVariant);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (DetailsViewModel.itemVariantFromMap != null) {
            detailsViewModel = new DetailsViewModel(DetailsViewModel.itemVariantFromMap);
            init();
            DetailsViewModel.itemVariantFromMap = null;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_stationery, R.anim.slide_down);
    }

    @Override
    public void onBackPressed() {
        if (isExpanded) {
            isExpanded = false;
            setDynamicHeights();
            return;
        }

        super.onBackPressed();
        detailsViewModel.addViewHistory();
    }

    /**
     * Initialisation
     */
    private void init() {
        imgIndex = 0;
        initStyling();
        initAdaptors();
        initCarouselAdaptor();
        initListeners();
    }

    private void initStyling() {
        vh.detailsContainer.setMinimumHeight(Display.getScreenHeight(vh.detailsContainer));
        vh.itemTitle.setText(detailsViewModel.getItemName());
        vh.storeName.setText(detailsViewModel.getStoreBranchName());
        updateHeartIcon();

        switch (detailsViewModel.getItemCategory()) {
            case laptops:
                initSpecsStyling(CategoryType.laptops);
                break;
            case phones:
                vh.ramContainer.setVisibility(View.GONE);
                initSpecsStyling(CategoryType.phones);
                break;
            case accessories:
                vh.ramContainer.setVisibility(View.GONE);
                vh.storageContainer.setVisibility(View.GONE);
                initSpecsStyling(CategoryType.accessories);
                break;
        }

        setDynamicHeights();
        setDynamicStyling();
    }

    private void initSpecsStyling(CategoryType category) {
        List<SpecsType> fixedSpecs;

        if (category.equals(CategoryType.laptops)) {
            fixedSpecs = Laptop.FIXED_SPECS;
        } else if (category.equals(CategoryType.phones)) {
            fixedSpecs = Phone.FIXED_SPECS;
        } else {
            vh.specs.setVisibility(View.GONE);
            vh.recContainer.setVisibility(View.GONE);
            vh.summaryText.setText(detailsViewModel.getItemSpecs().getFixedSpecs().get(SpecsType.summary.name()));
            return;
        }

        vh.summaryText.setVisibility(View.GONE);

        DetailsSpecsAdaptor detailsSpecsAdaptor =
                new DetailsSpecsAdaptor(this, R.layout.item_details_description,
                        fixedSpecs, detailsViewModel.getItemSpecs().getFixedSpecs());
        vh.specsList.setAdapter(detailsSpecsAdaptor);
        ListUtil.setDynamicHeight(vh.specsList);
    }

    private void initAdaptors() {
        /**
         * COLOURS ADAPTOR
         */
        LinearLayoutManager coloursLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.coloursList.setLayoutManager(coloursLayoutManager);

        List<Colour> colours = detailsViewModel.getItemColours();
        DetailsColorAdaptor coloursAdaptor =
                new DetailsColorAdaptor(this, colours,
                        detailsViewModel.getItemVariant(), this::updateAdaptor);
        vh.coloursList.setAdapter(coloursAdaptor);

        /**
         * CUSTOMISATION ADAPTOR
         */
        LinearLayoutManager storageLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.storageList.setLayoutManager(storageLayoutManager);

        List<SpecsOption> storageOptions = detailsViewModel.getStorageOptions();
        if (storageOptions != null) {
            DetailsCustomisationAdaptor storageAdaptor =
                    new DetailsCustomisationAdaptor(this, storageOptions,
                            SpecsOptionType.storage, detailsViewModel.getItemVariant(), this::updateAdaptor);
            vh.storageList.setAdapter(storageAdaptor);
        }

        LinearLayoutManager ramLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        vh.ramList.setLayoutManager(ramLayoutManager);

        List<SpecsOption> ramOptions = detailsViewModel.getRamOptions();
        if (ramOptions != null) {
            DetailsCustomisationAdaptor ramAdaptor =
                    new DetailsCustomisationAdaptor(this, ramOptions,
                            SpecsOptionType.ram, detailsViewModel.getItemVariant(), this::updateAdaptor);
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

    private void setDynamicHeights() {
        int oldHeight = (int) (heightFactor * (Display.getScreenHeight(vh.transparentContainer)));

        if (isExpanded) {
            heightFactor = 1;
        } else {
            switch (detailsViewModel.getItemCategory()) {
                case phones:
                    //0.35 too small
                    heightFactor = Display.getDynamicHeight(vh.transparentContainer, 0.43, 0.55);
                    break;
                case laptops:
                    heightFactor = Display.getDynamicHeight(vh.transparentContainer, 0.32, 0.45);
                    break;
                case accessories:
                default:
                    heightFactor = Display.getDynamicHeight(vh.transparentContainer, 0.47, 0.58);
            }
        }

        int newHeight = (int) (heightFactor * (Display.getScreenHeight(vh.transparentContainer)));

        ValueAnimator anim = ValueAnimator.ofFloat(oldHeight, newHeight);
        anim.addUpdateListener(valueAnimator -> {
            float val = (Float) valueAnimator.getAnimatedValue();
            ResponsiveView.setHeight((int) val, vh.carouselContainer, vh.transparentContainer);
        });

        anim.setDuration(ANIMATION_INTERVAL);
        anim.start();
    }

    private void setDynamicStyling() {
        vh.colourTitle.setText(detailsViewModel.getVariantColourInString());
        vh.priceText.setText(detailsViewModel.getItemVariantPriceInString());
        updateHeartIcon();
    }

    private void updateHeartIcon() {
        vh.heartFilledBtn.setVisibility(detailsViewModel.isInWishlist() ? View.VISIBLE : View.INVISIBLE);
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

        new TabLayoutMediator(vh.dots, vh.carousel, (tab, position) -> {}).attach();
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
            overridePendingTransition(R.anim.slide_up, R.anim.slide_stationery);
        });

        vh.addCartBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
            detailsViewModel.addCart();
        });

        vh.closeBtn.setOnClickListener(v -> {
            if (isExpanded) {
                isExpanded = false;
                setDynamicHeights();
                return;
            }

            detailsViewModel.addViewHistory();

            super.onBackPressed();
            this.finish();
        });

        vh.heartHollowBtn.setOnClickListener(v -> {
            detailsViewModel.addWishlist();
            updateHeartIcon();
        });

        vh.heartFilledBtn.setOnClickListener(v -> {
            detailsViewModel.removeWishlist();
            updateHeartIcon();
        });

        vh.transparentContainer.setOnTouchListener((view, motionEvent) -> {
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
//                            updateCarouselImages();
                    } else if (currentX > historicX) {
                        imgIndex--;
                        if (imgIndex < 0) imgIndex = 0;

                        vh.carousel.setCurrentItem(imgIndex);
//                            updateCarouselImages();
                    } else {
                        isExpanded = !isExpanded;
                        setDynamicHeights();
                    }

            }
            return false;
        });

        vh.scrollContainer.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    historicY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float currentY = motionEvent.getY();

                    if (currentY > historicY && !isExpanded && (vh.scrollContainer.getScrollY() == 0)) {
                        isExpanded = !isExpanded;
                        setDynamicHeights();
                    }
            }
            return false;
        });

        vh.scrollContainer.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            if (vh.scrollContainer.getScrollY() > ((Display.getScreenHeight(vh.scrollContainer) * heightFactor) - 100)) {
                vh.closeBtn.setVisibility(View.INVISIBLE);
            } else {
                vh.closeBtn.setVisibility(View.VISIBLE);
            }

            if (vh.scrollContainer.getScrollY() != 0) {
                vh.dots.setVisibility(View.INVISIBLE);

                if (isExpanded) {
                    isExpanded = false;
                    setDynamicHeights();
                }
            } else {
                vh.dots.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateAdaptor(IItemVariant itemVariant) {
        detailsViewModel.setItemVariant(itemVariant);
        initAdaptors();
        setDynamicStyling();

        if (!displayedColour.equals(itemVariant.getColour())) {
            //colour changed
            initCarouselAdaptor();
            displayedColour = itemVariant.getColour();
        }
    }
}

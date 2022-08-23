package com.example.nolo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.nolo.R;
import com.example.nolo.adaptors.CarouselPagerAdaptor;
import com.example.nolo.adaptors.DetailsColorAdaptor;
import com.example.nolo.adaptors.DetailsCustomisationAdaptor;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.SpecsOptionType;
import com.example.nolo.util.Display;
import com.example.nolo.viewmodels.DetailsViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DetailsActivity extends FragmentActivity {
    private DetailsViewModel detailsViewModel;

    private ViewHolder vh;
    private int imgIndex;
    private int maxIndex;
    private float historicX;

    private class ViewHolder {
        HorizontalScrollView transparentContainer;
        LinearLayout detailsContainer, ramContainer, storageContainer, specs, protectionSpecs, gpuSpecs, ramSpecs, keyboardSpecs, communicationSpecs, fingerprintSpecs, opticalSpecs, portsSpecs, sensorsSpecs, simSpecs, acSpecs;
        TextView itemTitle, colourTitle, quantityText, storeName, priceText, displayText, protectionText, dimenText, weightText, cpuText, gpuText, ramText, storageText, cameraText, keyboardText, communicationText, audioText, touchscreenText, fingerprintText, opticalText, portsText, batteryText, sensorsText, osText, simText, acText;;
        RelativeLayout decrementBtn, incrementBtn;
        RecyclerView coloursList, ramList, storageList;
        ImageView closeBtn, storesBtn;
        MaterialButton addCartBtn;
        ViewPager2 carousel;

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
            protectionSpecs = findViewById(R.id.protection_specs);
            gpuSpecs = findViewById(R.id.gpu_specs);
            ramSpecs = findViewById(R.id.ram_specs);
            keyboardSpecs = findViewById(R.id.keyboard_specs);
            communicationSpecs = findViewById(R.id.communication_specs);
            fingerprintSpecs = findViewById(R.id.fingerprint_specs);
            opticalSpecs = findViewById(R.id.optical_specs);
            portsSpecs = findViewById(R.id.ports_specs);
            sensorsSpecs = findViewById(R.id.sensors_specs);
            simSpecs = findViewById(R.id.sim_specs);
            acSpecs = findViewById(R.id.ac_specs);

            displayText = findViewById(R.id.display_text);
            protectionText = findViewById(R.id.protection_text);
            dimenText = findViewById(R.id.dimen_text);
            weightText = findViewById(R.id.weight_text);
            cpuText = findViewById(R.id.cpu_text);
            gpuText = findViewById(R.id.gpu_text);
            ramText = findViewById(R.id.ram_text);
            storageText = findViewById(R.id.storage_text);
            cameraText = findViewById(R.id.camera_text);
            keyboardText = findViewById(R.id.keyboard_text);
            communicationText = findViewById(R.id.communication_text);
            audioText = findViewById(R.id.audio_text);
            touchscreenText = findViewById(R.id.touchscreen_text);
            fingerprintText = findViewById(R.id.fingerprint_text);
            opticalText = findViewById(R.id.optical_text);
            portsText = findViewById(R.id.ports_text);
            batteryText = findViewById(R.id.battery_text);
            sensorsText = findViewById(R.id.sensors_text);
            osText = findViewById(R.id.os_text);
            simText = findViewById(R.id.sim_text);
            acText = findViewById(R.id.ac_text);
            carousel = findViewById(R.id.carousel);
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
         * CAROUSEL
         */

        List<String> uris = detailsViewModel.getImageUrisByColour();
        maxIndex = uris.size() - 1;
        CarouselPagerAdaptor pagerAdapter = new CarouselPagerAdaptor(this, uris);
        vh.carousel.setAdapter(pagerAdapter);
        vh.carousel.setCurrentItem(imgIndex, false);

    }

    @Override
    public void onBackPressed() {
        if (vh.carousel.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            imgIndex--;
            if (imgIndex < 0) imgIndex = 0;
            vh.carousel.setCurrentItem(imgIndex);
            System.out.println("SWIPE: " + imgIndex);
        }
    }

    private void initStyling() {
        double heightFactor = 0.45;

        vh.detailsContainer.setMinimumHeight(Display.getScreenHeight(vh.detailsContainer));
        vh.itemTitle.setText(detailsViewModel.getItemName());
        vh.storeName.setText(detailsViewModel.getStoreBranchName());

        switch (detailsViewModel.getItemCategory()) {
            case laptops:
                initSpecsStyling(CategoryType.laptops);
                break;
            case phones:
                heightFactor = 0.7;
                vh.ramContainer.setVisibility(View.INVISIBLE);
                initSpecsStyling(CategoryType.phones);
                break;
            case accessories:
                vh.ramContainer.setVisibility(View.INVISIBLE);
                vh.storageContainer.setVisibility(View.INVISIBLE);
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
            detailsViewModel.addCart();
        });

        vh.closeBtn.setOnClickListener(v -> {
            super.onBackPressed();
            this.finish();
        });

        vh.transparentContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                System.out.println("SWIPE: " + motionEvent.getAction());
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        historicX = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float currentX = motionEvent.getX();

                        if (currentX < historicX){
                            imgIndex++;
                            if (imgIndex > maxIndex) imgIndex = maxIndex;
                            vh.carousel.setCurrentItem(imgIndex);
                            System.out.println("SWIPE: " + imgIndex);
                        } else if (currentX > historicX){
                            imgIndex--;
                            if (imgIndex < 0) imgIndex = 0;
                            vh.carousel.setCurrentItem(imgIndex);
                            System.out.println("SWIPE: " + imgIndex);
                        }

                }
                return false;
            }
        });
    }

    private void updateAdaptor(IItemVariant itemVariant) {
        detailsViewModel.setItemVariant(itemVariant);
        initAdaptors();
        setDynamicStyling();
        initSpecsStyling(detailsViewModel.getItemCategory());
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

        imgIndex = 0;

        initStyling();
        initAdaptors();
        initListeners();
    }

    private void initSpecsStyling(CategoryType category) {
        if (category.equals(CategoryType.laptops)) {
            vh.gpuText.setText(detailsViewModel.getItemSpecs().getGpu());
            vh.ramText.setText(String.valueOf(detailsViewModel.getItemVariant().getRamOption().getSize()) + "GB RAM");
            vh.communicationText.setText(detailsViewModel.getItemSpecs().getCommunication());
            vh.fingerprintText.setText(detailsViewModel.getItemSpecs().getFingerprintReader());
            vh.opticalText.setText(detailsViewModel.getItemSpecs().getOpticalDrive());
            vh.portsText.setText(detailsViewModel.getItemSpecs().getPorts());
            vh.keyboardText.setText(detailsViewModel.getItemSpecs().getKeyboard());
            vh.acText.setText(detailsViewModel.getItemSpecs().getAcAdaptor());

            vh.protectionSpecs.setVisibility(View.GONE);
            vh.sensorsSpecs.setVisibility(View.GONE);
            vh.simSpecs.setVisibility(View.GONE);
        } else if (category.equals(CategoryType.phones)) {
            vh.protectionText.setText(detailsViewModel.getItemSpecs().getProtectionResistance());
            vh.sensorsText.setText(detailsViewModel.getItemSpecs().getSensors());
            vh.simText.setText(detailsViewModel.getItemSpecs().getSimCard());

            vh.gpuSpecs.setVisibility(View.GONE);
            vh.ramSpecs.setVisibility(View.GONE);
            vh.communicationSpecs.setVisibility(View.GONE);
            vh.fingerprintSpecs.setVisibility(View.GONE);
            vh.opticalSpecs.setVisibility(View.GONE);
            vh.portsSpecs.setVisibility(View.GONE);
            vh.keyboardSpecs.setVisibility(View.GONE);
            vh.acSpecs.setVisibility(View.GONE);
        } else {
            vh.specs.setVisibility(View.GONE);
            return;
        }

        vh.displayText.setText(detailsViewModel.getItemSpecs().getDisplay());
        vh.dimenText.setText(detailsViewModel.getItemSpecs().getDimensions());
        vh.weightText.setText(detailsViewModel.getItemSpecs().getWeight());
        vh.cpuText.setText(detailsViewModel.getItemSpecs().getCpu());
        vh.storageText.setText(String.valueOf(detailsViewModel.getItemVariant().getStorageOption().getSize()) + "GB SSD");
        vh.cameraText.setText(detailsViewModel.getItemSpecs().getCamera());
        vh.audioText.setText(detailsViewModel.getItemSpecs().getAudio());
        vh.touchscreenText.setText(detailsViewModel.getItemSpecs().getTouchscreen());
        vh.batteryText.setText(detailsViewModel.getItemSpecs().getBattery());
        vh.osText.setText(detailsViewModel.getItemSpecs().getOperatingSystem());
    }
}

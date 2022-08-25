package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.specs.PhoneSpecs;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.SpecsType;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Phone extends Item {
    public static final List<SpecsType> SPECS = new ArrayList<>(Arrays.asList(
            SpecsType.summary,
            SpecsType.operatingSystem,
            SpecsType.display,
            SpecsType.cpu,
            SpecsType.camera,
            SpecsType.communication,
            SpecsType.audio,
            SpecsType.touchscreen,
            SpecsType.protectionResistance,
            SpecsType.simCard,
            SpecsType.sensors,
            SpecsType.battery,
            SpecsType.dimensions,
            SpecsType.weight
    ));
    private List<String> recommendedAccessoryIds;
    private PhoneSpecs phoneSpecs;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Phone() {}

    public Phone(String name, String brand, PhoneSpecs phoneSpecs, List<StoreVariant> storeVariants,
                 List<String> imageUris, List<String> recommendedAccessoryIds) {
        super(CategoryType.phones, name, brand, storeVariants, imageUris);
        this.recommendedAccessoryIds = recommendedAccessoryIds;
        this.phoneSpecs = phoneSpecs;
    }

    @Override
    public List<String> getRecommendedAccessoryIds() {
        return recommendedAccessoryIds;
    }

    @NonNull
    @Override
    public String toString() {
        return "Phone{" +
                "itemId=" + getItemId() +
                '}';
    }

    @Override
    public PhoneSpecs getPhoneSpecs() {
        return phoneSpecs;
    }

    @Override
    @Exclude
    public ISpecs getSpecs() {
        return getPhoneSpecs();
    }

    /**
     * Get the default variant of the current item (Phone)
     *
     * @return Default variant of the phone
     */
    @Override
    @Exclude
    public IItemVariant getDefaultItemVariant() {
        IItemVariant itemVariant = super.getDefaultItemVariant();
        itemVariant.setStorageOption(phoneSpecs.getStorageOptions().get(0));

        return itemVariant;
    }
}

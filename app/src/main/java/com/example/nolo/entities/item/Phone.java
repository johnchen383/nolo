package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.specs.Specs;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.SpecsOptionType;
import com.example.nolo.enums.SpecsType;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Phone extends Item {
    public static final List<SpecsType> FIXED_SPECS = new ArrayList<>(Arrays.asList(
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

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Phone() {}

    public Phone(String name, String brand, Specs specs, List<StoreVariant> storeVariants,
                 List<String> imageUris, List<String> recommendedAccessoryIds) {
        super(CategoryType.phones, name, brand, specs, storeVariants, imageUris);
        this.recommendedAccessoryIds = recommendedAccessoryIds;
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

    /**
     * Get the default variant of the current item (Phone)
     *
     * @return Default variant of the phone
     */
    @Override
    @Exclude
    public IItemVariant getDefaultItemVariant() {
        IItemVariant itemVariant = super.getDefaultItemVariant();
        itemVariant.setStorageOption(getSpecs().getCustomisableSpecs().get(SpecsOptionType.storage.name()).get(0));

        return itemVariant;
    }
}

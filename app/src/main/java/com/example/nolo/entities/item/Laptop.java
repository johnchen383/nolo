package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.specs.Specs;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.SpecsOptionType;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Laptop extends Item {
    private List<String> recommendedAccessoryIds;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Laptop() {}

    public Laptop(String name, String brand, Specs specs, List<StoreVariant> storeVariants,
                  List<String> imageUris, List<String> recommendedAccessoryIds) {
        super(CategoryType.laptops, name, brand, specs, storeVariants, imageUris);
        this.recommendedAccessoryIds = recommendedAccessoryIds;
    }

    @Override
    public List<String> getRecommendedAccessoryIds() {
        return recommendedAccessoryIds;
    }

    @NonNull
    @Override
    public String toString() {
        return "Laptop{" +
                "brand=" + getBrand() +
                '}';
    }

    /**
     * Get the default variant of the current item (Laptop)
     *
     * @return Default variant of the laptop
     */
    @Override
    @Exclude
    public IItemVariant getDefaultItemVariant() {
        IItemVariant itemVariant = super.getDefaultItemVariant();
        itemVariant.setStorageOption(getSpecs().getCustomisableSpecs().get(SpecsOptionType.storage).get(0));
        itemVariant.setRamOption(getSpecs().getCustomisableSpecs().get(SpecsOptionType.ram).get(0));

        return itemVariant;
    }
}

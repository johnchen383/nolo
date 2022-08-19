package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.storevariants.IItemStoreVariant;
import com.example.nolo.enums.CategoryType;

import java.util.List;

public class Phone extends Item {
    private List<String> recommendedAccessoryIds;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Phone() {}

    public Phone(String name, String brand, ISpecs specs, List<IItemStoreVariant> storeVariants,
                 List<String> imageUris, List<String> recommendedAccessoryIds) {
        super(name, CategoryType.phones,brand, specs, storeVariants, imageUris);
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
}

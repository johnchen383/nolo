package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.specs.Specs;
import com.example.nolo.entities.item.storevariants.IStoreVariant;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.enums.CategoryType;

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
                "itemId=" + getItemId() +
                '}';
    }
}

package com.example.nolo.entities.item;

import com.example.nolo.entities.item.specs.ISpecs;

import java.util.List;

public class Laptop extends Item {
    private List<String> recommendedAccessories;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Laptop() {}

    public Laptop(String itemId, String name, String brand, ISpecs specs, List<IItemStoreVariant> storeVariants, List<String> imageUris, List<String> recommendedAccessories) {
        super(itemId, name, brand, specs, storeVariants, imageUris);
        this.recommendedAccessories = recommendedAccessories;
    }

    @Override
    public List<String> getRecommendedAccessories() {
        return recommendedAccessories;
    }
}

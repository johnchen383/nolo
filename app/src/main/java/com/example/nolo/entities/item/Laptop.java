package com.example.nolo.entities.item;

import java.util.List;

public class Laptop extends Item {
    private List<String> recommendedAccessories;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Laptop() {}

    public Laptop(String itemId, String category, String store, String specs, double price, List<String> imageUris, List<String> recommendedAccessories) {
        super(itemId, category, store, specs, price, imageUris);
        this.recommendedAccessories = recommendedAccessories;
    }

    @Override
    public List<String> getRecommendedAccessories() {
        return recommendedAccessories;
    }
}

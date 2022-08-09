package com.example.nolo.entities;

import java.util.List;

public class Laptop extends Item {
    private List<String> recommendedAccessories;

    public Laptop(String itemId, String category, String store, String specs, double price, List<String> imageUris, List<String> recommendedAccessories) {
        super(itemId, category, store, specs, price, imageUris);
        this.recommendedAccessories = recommendedAccessories;
    }

    @Override
    public List<String> getRecommendedAccessories() {
        return recommendedAccessories;
    }
}

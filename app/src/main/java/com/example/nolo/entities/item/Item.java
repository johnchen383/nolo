package com.example.nolo.entities.item;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public abstract class Item implements IItem {
    private String itemId, category, store, specs;
    private double price;
    private List<String> imageUris;

    public Item() {}

    public Item(String itemId, String category, String store, String specs, double price, List<String> imageUris) {
        this.itemId = itemId;
        this.category = category;
        this.store = store;
        this.specs = specs;
        this.price = price;
        this.imageUris = imageUris;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getStore() {
        return store;
    }

    @Override
    public String getSpecs() {
        return specs;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public List<String> getImageUris() {
        return imageUris;
    }

    @Exclude
    public List<String> getRecommendedAccessories() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }
}

package com.example.nolo.entities.item;

import com.example.nolo.entities.item.specs.ISpecs;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public abstract class Item implements IItem {
    private String itemId, categoryId, name, brand;
    private ISpecs specs;
    private List<IItemStoreVariant> storeVariants;
    private List<String> imageUris;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Item() {}

    public Item(String itemId, String name, String brand, ISpecs specs, List<IItemStoreVariant> storeVariants, List<String> imageUris) {
        this.itemId = itemId;
        this.name = name;
        this.brand = brand;
        this.specs = specs;
        this.storeVariants = storeVariants;
        this.imageUris = imageUris;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    @Exclude
    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public ISpecs getSpecs() {
        return specs;
    }

    @Override
    public List<IItemStoreVariant> getStoreVariants() {
        return storeVariants;
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

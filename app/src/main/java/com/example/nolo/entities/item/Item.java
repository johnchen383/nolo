package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.repositories.CategoryType;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public abstract class Item implements IItem {
    // {itemId, categoryType} will not be in the Firestore
    private String itemId, name, brand;
    private CategoryType categoryType;
    private ISpecs specs;
    private List<IItemStoreVariant> storeVariants;
    private List<String> imageUris;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Item() {}

    public Item(String name, CategoryType categoryType, String brand, ISpecs specs, List<IItemStoreVariant> storeVariants, List<String> imageUris) {
        this.name = name;
        this.categoryType = categoryType;
        this.brand = brand;
        this.specs = specs;
        this.storeVariants = storeVariants;
        this.imageUris = imageUris;
    }

    @Override
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    @Exclude
    public String getItemId() {
        return itemId;
    }

    @Override
    @Exclude
    public CategoryType getCategoryType() {
        return categoryType;
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

    @NonNull
    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", name='" + name + '\'' +
                ", categoryType=" + categoryType +
                '}';
    }
}

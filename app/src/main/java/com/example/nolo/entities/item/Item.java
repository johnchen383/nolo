package com.example.nolo.entities.item;

import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.storevariants.IItemStoreVariant;
import com.example.nolo.enums.CategoryType;
import com.google.firebase.firestore.Exclude;

import java.util.List;

/**
 * {@link #itemId} {@link #categoryType} will not be in the Firestore
 */
public abstract class Item implements IItem {
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
    public List<String> getRecommendedAccessoryIds() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    /**
     * Get the base price of the current item with the selected store ID
     *
     * @param storeId Selected store ID
     * @return base price of the item
     *         -1 if it is not the selected store ID is not found
     */
    @Override
    @Exclude
    public double getBasePrice(String storeId) {
        for (IItemStoreVariant isv : storeVariants) {
            if (isv.getStoreId().equals(storeId)) {
                return isv.getBasePrice();
            }
        }
        return -1;
    }
}

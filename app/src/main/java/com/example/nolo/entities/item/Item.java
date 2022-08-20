package com.example.nolo.entities.item;

import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.specs.Specs;
import com.example.nolo.entities.item.storevariants.IStoreVariant;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.enums.CategoryType;
import com.google.firebase.firestore.Exclude;

import java.util.List;

/**
 * {@link #itemId} {@link #categoryType} will not be in the Firestore
 */
public abstract class Item implements IItem {
    private String itemId, name, brand;
    private CategoryType categoryType;
    /**
     * Cannot use ISpecs and IStoreVariant (interfaces),
     * the reason is when the Firebase auto converts the data into
     * the object, it is unable to deserialize the object.
     * It is because the interface does not have 0-argument constructor.
     * To have the Firebase auto converts the data into the object,
     * our team decided to use StoreVariant and Specs.
     * So it is a reasonable excuse to violate the SOLID principle.
     */
    private Specs specs;
    private List<StoreVariant> storeVariants;
    private List<String> imageUris;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Item() {}

    public Item(CategoryType categoryType, String name, String brand, Specs specs, List<StoreVariant> storeVariants, List<String> imageUris) {
        this.categoryType = categoryType;
        this.name = name;
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
    public Specs getSpecs() {
        return specs;
    }

    @Override
    public List<StoreVariant> getStoreVariants() {
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
     * @return Base price of the item;
     *         -1 if the selected store ID is not found
     */
    @Override
    @Exclude
    public double getBasePrice(String storeId) {
        for (IStoreVariant isv : storeVariants) {
            if (isv.getStoreId().equals(storeId)) {
                return isv.getBasePrice();
            }
        }
        return -1;
    }
}

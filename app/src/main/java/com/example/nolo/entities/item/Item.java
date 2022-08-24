package com.example.nolo.entities.item;

import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.specs.AccessorySpecs;
import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.specs.LaptopSpecs;
import com.example.nolo.entities.item.specs.PhoneSpecs;
import com.example.nolo.entities.item.storevariants.IStoreVariant;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.entities.store.IBranch;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.interactors.store.GetStoreByIdUseCase;
import com.google.firebase.firestore.Exclude;

import java.util.List;

/**
 * {@link #itemId} {@link #categoryType} will not be in the Firestore
 */
public abstract class Item implements IItem {
    /**
     * Object list cannot use ISpecs and IStoreVariant (Specs and StoreVariant interfaces),
     * the reason is when the Firebase auto converts the data into
     * the object, it is unable to deserialize the object.
     * It is because the interface does not have 0-argument constructor.
     * To have the Firebase auto converts the data into the object,
     * our team decided to use StoreVariant and Specs.
     * So it is a reasonable excuse to violate the SOLID principle.
     */
    private String itemId, name, brand;
    private CategoryType categoryType;
    private List<StoreVariant> storeVariants;
    private List<String> imageUris;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Item() {}

    public Item(CategoryType categoryType, String name, String brand, List<StoreVariant> storeVariants, List<String> imageUris) {
        this.categoryType = categoryType;
        this.name = name;
        this.brand = brand;
        this.storeVariants = storeVariants;
        this.imageUris = imageUris;
    }

    @Override
    @Exclude
    public String getItemId() {
        return itemId;
    }

    @Override
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    @Exclude
    public CategoryType getCategoryType() {
        return categoryType;
    }

    @Override
    public void setCategoryType(CategoryType type) {
        this.categoryType = type;
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
    @Exclude
    public ISpecs getSpecs() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Override
    @Exclude
    public PhoneSpecs getPhoneSpecs() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Override
    @Exclude
    public LaptopSpecs getLaptopSpecs() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Override
    @Exclude
    public AccessorySpecs getAccessorySpecs() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Override
    public List<StoreVariant> getStoreVariants() {
        return storeVariants;
    }

    @Override
    public List<String> getImageUris() {
        return imageUris;
    }

    @Override
    @Exclude
    public List<String> getRecommendedAccessoryIds() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Override
    @Exclude
    public boolean getIsForLaptops() {
        throw new RuntimeException(this.getClass().getSimpleName() + " doesn't have this method");
    }

    @Override
    @Exclude
    public boolean getIsForPhones() {
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

    /**
     * Get the default variant of the current item
     *
     * @return Default variant of the current item
     */
    @Override
    @Exclude
    public IItemVariant getDefaultItemVariant() {
        IStoreVariant sVariant = this.storeVariants.get(0);
        IBranch branch = GetStoreByIdUseCase.getStoreById(sVariant.getStoreId()).getBranches().get(0);
        Colour colour = sVariant.getColours().get(0);

        return new ItemVariant(colour, this.itemId, this.categoryType, sVariant.getStoreId(), branch.getBranchName(), null, null);
    }
}

package com.example.nolo.entities.item.variant;

import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.specs.specsoption.ISpecsOption;
import com.example.nolo.enums.CategoryType;

import java.util.Objects;

/**
 * This class is for the selected Item.
 * E.g. Items in Viewed History.
 */
public class ItemVariant implements IItemVariant {
    private IColour colour;
    private CategoryType categoryType;
    private String itemId, storeId, branchName;
    private ISpecsOption storageOption, ramOption;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public ItemVariant() {}

    /**
     * This constructor is for Laptop, which has storage option and ram option.
     */
    public ItemVariant(IColour colour, String itemId, CategoryType categoryType, String storeId, String branchName,
                       ISpecsOption storageOption, ISpecsOption ramOption) {
        this.colour = colour;
        this.itemId = itemId;
        this.categoryType = categoryType;
        this.storeId = storeId;
        this.branchName = branchName;
        this.storageOption = storageOption;
        this.ramOption = ramOption;
    }

    /**
     * This constructor is for Phone, which has storage option.
     */
    public ItemVariant(IColour colour, String itemId, CategoryType categoryType, String storeId, String branchName,
                       ISpecsOption storageOption) {
        this(colour, itemId, categoryType, storeId, branchName, storageOption, null);
    }

    /**
     * This constructor is for Accessory.
     */
    public ItemVariant(IColour colour, String itemId, CategoryType categoryType, String storeId, String branchName) {
        this(colour, itemId, categoryType, storeId, branchName, null, null);
    }

    @Override
    public IColour getColour() {
        return colour;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public CategoryType getCategoryType() {
        return categoryType;
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public String getBranchName() {
        return branchName;
    }

    @Override
    public ISpecsOption getStorageOption() {
        return storageOption;
    }

    @Override
    public ISpecsOption getRamOption() {
        return ramOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemVariant that = (ItemVariant) o;
        return Objects.equals(colour, that.colour) && Objects.equals(itemId, that.itemId) && Objects.equals(storeId, that.storeId) && Objects.equals(branchName, that.branchName) && Objects.equals(storageOption, that.storageOption) && Objects.equals(ramOption, that.ramOption) && Objects.equals(categoryType, that.categoryType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colour, itemId, storeId, branchName, storageOption, ramOption, categoryType);
    }
}

package com.example.nolo.entities.item.variant;

import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.colour.IColour;
import com.example.nolo.entities.item.specs.specsoption.ISpecsOption;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.enums.CategoryType;

import java.util.Objects;

/**
 * This class is for the selected Item.
 * E.g. Items in Viewed History.
 */
public class ItemVariant implements IItemVariant {
    private Colour colour;
    private CategoryType categoryType;
    private String itemId, storeId, branchName;
    private SpecsOption storageOption, ramOption;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public ItemVariant() {}

    /**
     * This constructor is for Laptop, which has storage option and ram option.
     */
    public ItemVariant(Colour colour, String itemId, CategoryType categoryType, String storeId, String branchName,
                       SpecsOption storageOption, SpecsOption ramOption) {
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
    public ItemVariant(Colour colour, String itemId, CategoryType categoryType, String storeId, String branchName,
                       SpecsOption storageOption) {
        this(colour, itemId, categoryType, storeId, branchName, storageOption, null);
    }

    /**
     * This constructor is for Accessory.
     */
    public ItemVariant(Colour colour, String itemId, CategoryType categoryType, String storeId, String branchName) {
        this(colour, itemId, categoryType, storeId, branchName, null, null);
    }

    @Override
    public Colour getColour() {
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
    public SpecsOption getStorageOption() {
        return storageOption;
    }

    @Override
    public SpecsOption getRamOption() {
        return ramOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemVariant that = (ItemVariant) o;
        return Objects.equals(colour, that.colour)
                && categoryType == that.categoryType
                && Objects.equals(itemId, that.itemId)
                && Objects.equals(storeId, that.storeId)
                && Objects.equals(branchName, that.branchName)
                && Objects.equals(storageOption, that.storageOption)
                && Objects.equals(ramOption, that.ramOption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colour, categoryType, itemId, storeId, branchName, storageOption, ramOption);
    }
}

package com.example.nolo.entities.item.variant;

import com.example.nolo.entities.item.colour.IColour;

import java.util.Objects;

public class ItemVariant implements IItemVariant {
    private IColour colour;
    private String itemId;
    private String storeId;
    private String branchName;
    private String storageSize;  //TODO: replace with option
    private String ramSize;     //TODO: replace with option
    private String categoryId;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public ItemVariant() {}

    public ItemVariant(IColour colour, String itemId, String storeId, String branchName, String storageSize, String ramSize, String categoryId) {
        this.colour = colour;
        this.itemId = itemId;
        this.storeId = storeId;
        this.branchName = branchName;
        this.storageSize = storageSize;
        this.ramSize = ramSize;
        this.categoryId = categoryId;
    }

    public ItemVariant(IColour colour, String itemId, String storeId, String branchName, String categoryId){
        this(colour, itemId, storeId, branchName, null, null, categoryId);
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
    public String getCategoryId() {
        return categoryId;
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
    public String getStorageSize() {
        return storageSize;
    }

    @Override
    public String getRamSize() {
        return ramSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemVariant that = (ItemVariant) o;
        return Objects.equals(colour, that.colour) && Objects.equals(itemId, that.itemId) && Objects.equals(storeId, that.storeId) && Objects.equals(branchName, that.branchName) && Objects.equals(storageSize, that.storageSize) && Objects.equals(ramSize, that.ramSize) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colour, itemId, storeId, branchName, storageSize, ramSize, categoryId);
    }
}

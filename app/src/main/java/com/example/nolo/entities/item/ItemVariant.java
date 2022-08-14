package com.example.nolo.entities.item;

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
}

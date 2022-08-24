package com.example.nolo.entities.item.variant;

import android.util.Log;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.colour.Colour;
import com.example.nolo.entities.item.specs.specsoption.SpecsOption;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.interactors.item.GetItemByIdUseCase;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class is for the selected Item.
 * E.g. Items in Viewed History.
 */
public class ItemVariant implements IItemVariant, Serializable {
    /**
     * Cannot use IColour and ISpecsOption (interfaces),
     * the reason is when the Firebase auto converts the data into
     * the object, it is unable to deserialize the object.
     * It is because the interface does not have 0-argument constructor.
     * To have the Firebase auto converts the data into the object,
     * our team decided to use StoreVariant and Specs.
     * So it is a reasonable excuse to violate the SOLID principle.
     */
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
    public void setColour(Colour colour) {
        this.colour = colour;
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
    public void setStoreId(String id) {
        this.storeId = id;
    }

    @Override
    public String getBranchName() {
        return branchName;
    }

    @Override
    public void setBranchName(String name) {
        this.branchName = name;
    }

    @Override
    public SpecsOption getStorageOption() {
        return storageOption;
    }

    @Override
    public void setStorageOption(SpecsOption storageOption) {
        this.storageOption = storageOption;
    }

    @Override
    public SpecsOption getRamOption() {
        return ramOption;
    }

    @Override
    public void setRamOption(SpecsOption ramOption) {
        this.ramOption = ramOption;
    }

    /**
     * Get the item's name
     *
     * @return Item's name
     */
    @Override
    @Exclude
    public String getTitle() {
        return GetItemByIdUseCase.getItemById(itemId).getName();
    }

    /**
     * Get the item's price (base price + additional price) in String
     *
     * @return Item's price in String
     */
    @Override
    @Exclude
    public String getDisplayPrice() {
        return String.format("$%.2f", getNumericalPrice());
    }

    /**
     * Get the item's price (base price + additional price)
     *
     * @return Item's price
     */
    @Override
    @Exclude
    public double getNumericalPrice() {
        IItem item = GetItemByIdUseCase.getItemById(itemId);

        if (item == null) return 0;

        double displayPrice = item.getBasePrice(storeId);

        if (this.ramOption != null) {
            displayPrice += this.ramOption.getAdditionalPrice();
        }

        if (this.storageOption != null) {
            displayPrice += this.storageOption.getAdditionalPrice();
        }

        return displayPrice;
    }

    /**
     * Get the item's image with specific colour
     *
     * @return Item's image
     */
    @Override
    @Exclude
    public String getDisplayImage() {
        IItem item = GetItemByIdUseCase.getItemById(itemId);

        for (String uri : item.getImageUris()) {
            //find suffix which indicates colour
            String[] parts = uri.split("_");
            String suffix = parts[parts.length - 1];

            if (suffix.equals(colour.getName())) {
                return uri;
            }
        }

        Log.e("ItemVariant", "No image matched!");
        return item.getImageUris().get(0);
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

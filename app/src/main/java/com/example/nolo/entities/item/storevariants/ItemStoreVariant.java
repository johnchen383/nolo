package com.example.nolo.entities.item.storevariants;

import com.example.nolo.entities.item.colour.IColour;

import java.util.List;

/**
 * This class is for the variant of a specific item and a specific store.
 */
public class ItemStoreVariant implements IItemStoreVariant {
    private String storeId;
    private List<IColour> colours;
    private double basePrice;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public ItemStoreVariant() {}

    public ItemStoreVariant(String storeId, List<IColour> colours, double basePrice) {
        this.storeId = storeId;
        this.colours = colours;
        this.basePrice = basePrice;
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public List<IColour> getColours() {
        return colours;
    }

    @Override
    public double getBasePrice() {
        return basePrice;
    }
}

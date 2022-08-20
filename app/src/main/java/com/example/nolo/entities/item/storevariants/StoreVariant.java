package com.example.nolo.entities.item.storevariants;

import com.example.nolo.entities.item.colour.Colour;

import java.util.List;

/**
 * This class is for the variant of a specific item and a specific store.
 */
public class StoreVariant implements IStoreVariant {
    private String storeId;
    private List<Colour> colours;
    private double basePrice;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public StoreVariant() {}

    public StoreVariant(String storeId, List<Colour> colours, double basePrice) {
        this.storeId = storeId;
        this.colours = colours;
        this.basePrice = basePrice;
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public List<Colour> getColours() {
        return colours;
    }

    @Override
    public double getBasePrice() {
        return basePrice;
    }
}

package com.example.nolo.entities.item;

import com.example.nolo.entities.item.specs.IColour;

import java.util.List;

public class ItemStoreVariant implements IItemStoreVariant {
    private String storeId;
    private List<IColour> colours;
    private double basePrice;

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

package com.example.nolo.entities.item.storevariants;

import com.example.nolo.entities.item.colour.Colour;

import java.util.List;

/**
 * This class is for the variant of a specific item and a specific store.
 */
public class StoreVariant implements IStoreVariant {
    /**
     * Cannot use IColour (interface),
     * the reason is when the Firebase auto converts the data into
     * the object, it is unable to deserialize the object.
     * It is because the interface does not have 0-argument constructor.
     * To have the Firebase auto converts the data into the object,
     * our team decided to use StoreVariant and Specs.
     * So it is a reasonable excuse to violate the SOLID principle.
     */
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

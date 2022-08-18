package com.example.nolo.entities.item.specs.specsoption;

public class SpecsOption implements ISpecsOption {
    private int size;
    private double additionalPrice;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public SpecsOption() {}

    public SpecsOption(int size, double additionalPrice) {
        this.size = size;
        this.additionalPrice = additionalPrice;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public double getAdditionalPrice() {
        return additionalPrice;
    }
}
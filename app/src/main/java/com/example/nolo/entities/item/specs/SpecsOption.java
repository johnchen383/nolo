package com.example.nolo.entities.item.specs;

public class SpecsOption implements ISpecsOption {
    private int size;
    private double additionalPrice;

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

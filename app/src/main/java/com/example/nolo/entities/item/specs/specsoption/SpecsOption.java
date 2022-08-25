package com.example.nolo.entities.item.specs.specsoption;

import java.io.Serializable;
import java.util.Objects;

public class SpecsOption implements ISpecsOption, Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SpecsOption that = (SpecsOption) o;
        return Objects.equals(size, that.size)
                && Objects.equals(additionalPrice, that.additionalPrice);
    }
}

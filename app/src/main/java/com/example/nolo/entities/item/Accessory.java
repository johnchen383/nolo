package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.specs.Specs;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.enums.CategoryType;

import java.util.List;

public class Accessory extends Item {
    private boolean isForLaptops, isForPhones;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Accessory() {}

    public Accessory(String name, String brand, Specs specs, List<StoreVariant> storeVariants, List<String> imageUris, boolean isForLaptops, boolean isForPhones) {
        super(CategoryType.accessories, name, brand, specs, storeVariants, imageUris);
        this.isForLaptops = isForLaptops;
        this.isForPhones = isForPhones;
    }

    @NonNull
    @Override
    public String toString() {
        return "Accessory{" +
                "itemId=" + getItemId() +
                '}';
    }

    @Override
    public boolean getIsForLaptops() {
        return isForLaptops;
    }

    @Override
    public boolean getIsForPhones() {
        return isForPhones;
    }
}

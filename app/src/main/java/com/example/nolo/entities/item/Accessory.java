package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.specs.AccessorySpecs;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.enums.CategoryType;

import java.util.List;

public class Accessory extends Item {
    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Accessory() {}

    public Accessory(String name, String brand, AccessorySpecs specs, List<StoreVariant> storeVariants, List<String> imageUris) {
        super(CategoryType.accessories, name, brand, specs, storeVariants, imageUris);
    }

    @NonNull
    @Override
    public String toString() {
        return "Accessory{" +
                "itemId=" + getItemId() +
                '}';
    }
}

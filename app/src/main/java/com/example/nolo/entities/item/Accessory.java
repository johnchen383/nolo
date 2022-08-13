package com.example.nolo.entities.item;

import com.example.nolo.entities.item.specs.ISpecs;

import java.util.List;

public class Accessory extends Item {
    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Accessory() {}

    public Accessory(String itemId, String name, String brand, ISpecs specs, List<IItemStoreVariant> storeVariants, List<String> imageUris) {
        super(itemId, name, brand, specs, storeVariants, imageUris);
    }
}

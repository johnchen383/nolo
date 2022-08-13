package com.example.nolo.entities.item;

import java.util.List;

public class Accessory extends Item {
    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Accessory() {}

    public Accessory(String itemId, String store, String specs, double price, List<String> imageUris) {
        super(itemId, store, specs, price, imageUris);
    }
}

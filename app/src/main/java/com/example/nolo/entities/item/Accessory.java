package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.specs.AccessorySpecs;
import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.specs.LaptopSpecs;
import com.example.nolo.entities.item.specs.Specs;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.enums.CategoryType;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Accessory extends Item {
    private AccessorySpecs accessorySpecs;
    private boolean isForLaptops, isForPhones;
    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Accessory() {}

    public Accessory(String name, String brand, AccessorySpecs accessorySpecs, List<StoreVariant> storeVariants, List<String> imageUris, boolean isForLaptops, boolean isForPhones) {
        super(CategoryType.accessories, name, brand, storeVariants, imageUris);
        this.accessorySpecs = accessorySpecs;
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
    public AccessorySpecs getAccessorySpecs(){
        return accessorySpecs;
    }

    @Override
    @Exclude
    public ISpecs getSpecs(){
        return getAccessorySpecs();
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

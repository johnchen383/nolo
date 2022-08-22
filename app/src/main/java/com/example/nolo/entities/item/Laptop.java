package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.specs.LaptopSpecs;
import com.example.nolo.entities.item.specs.PhoneSpecs;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.enums.CategoryType;
import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Laptop extends Item {
    private List<String> recommendedAccessoryIds;
    private LaptopSpecs laptopSpecs;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Laptop() {}

    public Laptop(String name, String brand, LaptopSpecs laptopSpecs, List<StoreVariant> storeVariants,
                  List<String> imageUris, List<String> recommendedAccessoryIds) {
        super(CategoryType.laptops, name, brand, storeVariants, imageUris);
        this.recommendedAccessoryIds = recommendedAccessoryIds;
        this.laptopSpecs = laptopSpecs;
    }

    @Override
    public List<String> getRecommendedAccessoryIds() {
        return recommendedAccessoryIds;
    }

    @NonNull
    @Override
    public String toString() {
        return "Laptop{" +
                "brand=" + getBrand() +
                '}';
    }

    @Override
    public LaptopSpecs getLaptopSpecs(){
        return laptopSpecs;
    }

    @Override
    @Exclude
    public ISpecs getSpecs(){
        return getLaptopSpecs();
    }
}

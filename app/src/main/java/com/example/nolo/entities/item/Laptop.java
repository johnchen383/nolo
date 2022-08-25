package com.example.nolo.entities.item;

import androidx.annotation.NonNull;

import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.specs.LaptopSpecs;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.enums.SpecsType;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Laptop extends Item {
    public static final List<SpecsType> SPECS = new ArrayList<>(Arrays.asList(
            SpecsType.summary,
            SpecsType.operatingSystem,
            SpecsType.display,
            SpecsType.cpu,
            SpecsType.gpu,
            SpecsType.camera,
            SpecsType.keyboard,
            SpecsType.communication,
            SpecsType.audio,
            SpecsType.touchscreen,
            SpecsType.fingerprintReader,
            SpecsType.opticalDrive,
            SpecsType.ports,
            SpecsType.battery,
            SpecsType.acAdaptor,
            SpecsType.dimensions,
            SpecsType.weight
    ));
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
    public LaptopSpecs getLaptopSpecs() {
        return laptopSpecs;
    }

    @Override
    @Exclude
    public ISpecs getSpecs() {
        return getLaptopSpecs();
    }

    /**
     * Get the default variant of the current item (Laptop)
     *
     * @return Default variant of the laptop
     */
    @Override
    @Exclude
    public IItemVariant getDefaultItemVariant() {
        IItemVariant itemVariant = super.getDefaultItemVariant();
        itemVariant.setStorageOption(laptopSpecs.getStorageOptions().get(0));
        itemVariant.setRamOption(laptopSpecs.getRamOptions().get(0));

        return itemVariant;
    }
}

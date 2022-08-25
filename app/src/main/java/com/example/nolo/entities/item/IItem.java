package com.example.nolo.entities.item;

import com.example.nolo.entities.item.specs.Specs;
import com.example.nolo.entities.item.storevariants.StoreVariant;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.enums.CategoryType;

import java.util.List;

public interface IItem {
    String getItemId();
    void setItemId(String itemId);
    CategoryType getCategoryType();
    void setCategoryType(CategoryType type);
    String getName();
    String getBrand();
    Specs getSpecs();
    List<StoreVariant> getStoreVariants();
    List<String> getImageUris();
    List<String> getRecommendedAccessoryIds();
    double getBasePrice(String storeId);
    IItemVariant getDefaultItemVariant();
    boolean getIsForLaptops();
    boolean getIsForPhones();
}

package com.example.nolo.entities.item;

import com.example.nolo.entities.item.specs.ISpecs;
import com.example.nolo.entities.item.storevariants.IItemStoreVariant;
import com.example.nolo.enums.CategoryType;

import java.util.List;

public interface IItem {
    void setItemId(String itemId);
    String getItemId();
    CategoryType getCategoryType();
    String getName();
    String getBrand();
    ISpecs getSpecs();
    List<IItemStoreVariant> getStoreVariants();
    List<String> getImageUris();
    List<String> getRecommendedAccessoryIds();
    double getBasePrice(String storeId);
}

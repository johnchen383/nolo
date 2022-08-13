package com.example.nolo.entities.item;

import com.example.nolo.entities.item.specs.ISpecs;

import java.util.List;

public interface IItem {
    void setItemId(String itemId);
    String getItemId();
    void setCategoryId(String categoryId);
    String getCategoryId();
    String getName();
    String getBrand();
    ISpecs getSpecs();
    List<IItemStoreVariant> getStoreVariants();
    List<String> getImageUris();
    List<String> getRecommendedAccessories();
}

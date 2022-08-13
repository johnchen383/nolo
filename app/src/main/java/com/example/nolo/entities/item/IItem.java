package com.example.nolo.entities.item;

import java.util.List;

public interface IItem {
    String getItemId();
    void setCategoryId(String categoryId);
    String getCategoryId();
    double getPrice();
    String getStore();
    String getSpecs();
    List<String> getImageUris();
    List<String> getRecommendedAccessories();
}

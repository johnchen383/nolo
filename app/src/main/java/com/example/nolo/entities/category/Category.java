package com.example.nolo.entities.category;

import androidx.annotation.NonNull;

import com.example.nolo.entities.store.IBranch;

public class Category implements ICategory {
    private String categoryId, categoryName, imageUri;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Category() {}

    public Category(String categoryId, String categoryName, String imageUri) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.imageUri = imageUri;
    }

    @Override
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String getImageUri() {
        return imageUri;
    }

    @NonNull
    @Override
    public String toString() {
        String result = "Category ID: " + categoryId;
        return result;
    }
}

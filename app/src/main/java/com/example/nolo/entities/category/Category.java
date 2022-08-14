package com.example.nolo.entities.category;

import androidx.annotation.NonNull;

import com.example.nolo.repositories.CategoryType;
import com.google.firebase.firestore.Exclude;

public class Category implements ICategory {
    private CategoryType categoryType;
    private String categoryName, imageUri;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Category() {}

    public Category(String categoryName, String imageUri) {
        this.categoryName = categoryName;
        this.imageUri = imageUri;
    }

    @Override
    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    @Override
    @Exclude
    public CategoryType getCategoryType() {
        return categoryType;
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
        String result = "Category ID: " + categoryType;
        return result;
    }
}

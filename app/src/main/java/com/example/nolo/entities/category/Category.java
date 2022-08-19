package com.example.nolo.entities.category;

import androidx.annotation.NonNull;

import com.example.nolo.enums.CategoryType;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Category implements ICategory, Serializable {
    private CategoryType categoryType;
    private String imageUri;

    /**
     * 0 argument constructor for convert Firebase data to this class
     */
    public Category() {}

    public Category(CategoryType categoryType, String imageUri) {
        this.categoryType = categoryType;
        this.imageUri = imageUri;
    }

    @Override
    public CategoryType getCategoryType() {
        return categoryType;
    }

    @Override
    public String getImageUri() {
        return imageUri;
    }

    @Override
    @Exclude
    public String getCategoryName() {
        return categoryType.name();
    }

    @NonNull
    @Override
    public String toString() {
        return "Category type: " + categoryType;
    }
}

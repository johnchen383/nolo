package com.example.nolo.entities.category;

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
}

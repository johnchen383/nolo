package com.example.nolo.entities.category;

import com.example.nolo.repositories.CategoryType;

public interface ICategory {
    CategoryType getCategoryType();
    String getCategoryName();
    String getImageUri();
    void setCategoryType(CategoryType categoryType);
}

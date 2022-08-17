package com.example.nolo.entities.category;

import com.example.nolo.enums.CategoryType;

public interface ICategory {
    void setCategoryType(CategoryType categoryType);
    CategoryType getCategoryType();
    String getCategoryName();
    String getImageUri();
}

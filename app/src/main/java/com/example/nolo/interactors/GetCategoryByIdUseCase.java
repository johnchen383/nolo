package com.example.nolo.interactors;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.repositories.CategoryType;
import com.example.nolo.repositories.category.CategoriesRepository;

public class GetCategoryByIdUseCase {
    public static ICategory getCategoryById(CategoryType categoryType) {
        return CategoriesRepository.getInstance().getCategoryById(categoryType);
    }
}

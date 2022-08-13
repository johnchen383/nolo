package com.example.nolo.interactors;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.repositories.category.CategoriesRepository;

public class GetCategoryByIdUseCase {
    public static ICategory getCategoryById(String categoryId) {
        return CategoriesRepository.getInstance().getCategoryById(categoryId);
    }
}
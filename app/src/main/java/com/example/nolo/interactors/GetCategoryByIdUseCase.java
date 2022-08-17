package com.example.nolo.interactors;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.repositories.category.CategoriesRepository;

public class GetCategoryByIdUseCase {
    /**
     *
     * @param categoryId
     * @return
     */
    public static ICategory getCategoryById(CategoryType categoryType) {
        return CategoriesRepository.getInstance().getCategoryById(categoryType);
    }
}

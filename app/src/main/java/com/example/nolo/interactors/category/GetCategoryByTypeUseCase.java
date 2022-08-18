package com.example.nolo.interactors.category;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.repositories.category.CategoriesRepository;

public class GetCategoryByTypeUseCase {
    /**
     * Get Category entity by Category Type enum
     *
     * @param categoryType Specific Category Type in enum
     * @return Category entity if categoryType exists;
     *         Otherwise null
     */
    public static ICategory getCategoryByType(CategoryType categoryType) {
        return CategoriesRepository.getInstance().getCategoryByType(categoryType);
    }
}

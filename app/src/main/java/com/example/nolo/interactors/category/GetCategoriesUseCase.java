package com.example.nolo.interactors.category;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.repositories.category.CategoriesRepository;

import java.util.List;

public class GetCategoriesUseCase {
    /**
     * Get List of Category entities
     *
     * @return List of Category entities
     */
    public static List<ICategory> getCategories(){
        return CategoriesRepository.getInstance().getCategories();
    }
}

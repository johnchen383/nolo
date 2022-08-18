package com.example.nolo.interactors.category;

import com.example.nolo.repositories.category.CategoriesRepository;

import java.util.function.Consumer;

public class LoadCategoriesRepositoryUseCase {
    /**
     * Load data from Firebase
     *
     * @param onLoadedRepository A function that will run after the repository is loaded
     */
    public static void loadCategoriesRepository(Consumer<Class<?>> onLoadedRepository) {
        CategoriesRepository.getInstance().loadCategories(onLoadedRepository);
    }
}

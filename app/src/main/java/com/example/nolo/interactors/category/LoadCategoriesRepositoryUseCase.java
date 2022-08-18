package com.example.nolo.interactors.category;

import com.example.nolo.repositories.category.CategoriesRepository;

import java.util.function.Consumer;

public class LoadCategoriesRepositoryUseCase {
    /**
     *
     * @param loadedRepository
     */
    public static void loadCategoriesRepository(Consumer<Class<?>> loadedRepository) {
        CategoriesRepository.getInstance().loadCategories(loadedRepository);
    }
}

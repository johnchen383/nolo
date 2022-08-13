package com.example.nolo.interactors;

import com.example.nolo.repositories.category.CategoriesRepository;

import java.util.function.Consumer;

public class LoadCategoriesRepositoryUseCase {
    public static void loadCategoriesRepository(Consumer<Class<?>> loadedRepository) {
        CategoriesRepository.getInstance().loadCategories(loadedRepository);
    }
}
package com.example.nolo.repositories.category;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.store.IStore;

import java.util.List;
import java.util.function.Consumer;

public interface ICategoriesRepository {
    void loadCategories(Consumer<Class<?>> loadedRepository);
    ICategory getCategoryById(String categoryId);
    List<ICategory> getCategories();
}

package com.example.nolo.repositories.category;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.entities.store.IStore;
import com.example.nolo.repositories.CategoryType;

import java.util.List;
import java.util.function.Consumer;

public interface ICategoriesRepository {
    void loadCategories(Consumer<Class<?>> loadedRepository);
    ICategory getCategoryById(CategoryType categoryType);
    List<ICategory> getCategories();
}

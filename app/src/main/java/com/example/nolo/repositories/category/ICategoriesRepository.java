package com.example.nolo.repositories.category;

import com.example.nolo.entities.category.ICategory;
import com.example.nolo.enums.CategoryType;

import java.util.List;
import java.util.function.Consumer;

public interface ICategoriesRepository {
    void loadCategories(Consumer<Class<?>> onLoadedRepository);
    ICategory getCategoryByType(CategoryType categoryType);
    List<ICategory> getCategories();
}

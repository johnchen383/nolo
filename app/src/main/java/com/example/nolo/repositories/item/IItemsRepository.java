package com.example.nolo.repositories.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.enums.CategoryType;

import java.util.List;
import java.util.function.Consumer;

public interface IItemsRepository {
    void loadItems(Consumer<Class<?>> onLoadedRepository);
    List<IItem> getAllItems();
    IItem getItemById(String itemId);
    List<IItem> getCategoryItems(CategoryType categoryType);
}

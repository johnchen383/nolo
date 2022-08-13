package com.example.nolo.repositories.item;

import com.example.nolo.entities.item.IItem;

import java.util.List;
import java.util.function.Consumer;

public interface IItemsRepository {
    void loadItems(Consumer<Class<?>> loadedRepository);
    List<IItem> getAllItems();
    IItem getItemById(String itemId);
}

package com.example.nolo.repositories.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.enums.CategoryType;

import java.util.List;
import java.util.function.Consumer;

public interface IItemsRepository {
    void loadItems(Consumer<Class<?>> loadedRepository);
    List<IItem> getAllItems();
    IItem getItemById(String itemId);
    List<IItem> getItemByIdList(List<String> itemIds);
    List<IItem> getCategoryItems(CategoryType categoryType);
    List<IItem> getSearchSuggestions(String searchTerm);
    List<IItem> getAccessRecommendationsByItemId(String itemId);
}

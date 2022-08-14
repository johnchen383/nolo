package com.example.nolo.interactors;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.repositories.CategoryType;
import com.example.nolo.repositories.item.ItemsRepository;

import java.util.List;

public class GetCategoryItemsUseCase {
    public static List<IItem> getCategoryItems(CategoryType categoryType) {
        return ItemsRepository.getInstance().getCategoryItems(categoryType);
    }
}

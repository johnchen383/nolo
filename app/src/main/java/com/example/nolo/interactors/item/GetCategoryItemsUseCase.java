package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.enums.CategoryType;
import com.example.nolo.repositories.item.ItemsRepository;

import java.util.List;

public class GetCategoryItemsUseCase {
    /**
     * Get list of Items by a specific Category Type
     *
     * @param categoryType Specific Category Type
     * @return List of Items if categoryType exists;
     *         Otherwise empty list
     */
    public static List<IItem> getCategoryItems(CategoryType categoryType) {
        return ItemsRepository.getInstance().getCategoryItems(categoryType);
    }
}

package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.enums.CategoryType;

import java.util.ArrayList;
import java.util.List;

public class GetAccessRecommendationsByItemIdUseCase {
    /**
     * Get list of Accessory ItemVariant that are recommended of a specific Item
     *
     * @param itemId Specific Item ID
     * @return List of Accessory ItemVariant
     */
    public static List<ItemVariant> getAccessRecommendationsByItemId(String itemId) {
        List<IItem> accessories = GetCategoryItemsUseCase.getCategoryItems(CategoryType.accessories);
        List<String> recIds = GetItemByIdUseCase.getItemById(itemId).getRecommendedAccessoryIds();
        List<ItemVariant> result = new ArrayList<>();

        for (IItem item : accessories) {
            if (recIds.contains(item.getItemId())) {
                result.add((ItemVariant) item.getDefaultItemVariant());
            }
        }

        return result;
    }
}

package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.repositories.item.ItemsRepository;

import java.util.List;

public class GetAccessRecommendationsByItemIdUseCase {
    /**
     * Get list of Accessory ItemVariant that are recommended of a specific Item
     *
     * @param itemId Specific Item ID
     * @return List of Accessory ItemVariant
     */
    public static List<ItemVariant> getAccessRecommendationsByItemId(String itemId) {
        return ItemsRepository.getInstance().getAccessRecommendationsByItemId(itemId);
    }
}

package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.repositories.item.ItemsRepository;

import java.util.List;

public class GetAccessRecommendationsByItemIdUseCase {
    /**
     * Get list of Accessory entities that are recommended of a specific Item
     *
     * @param itemId Specific Item ID
     * @return List of Accessory entities
     */
    public static List<IItem> getAccessRecommendationsByItemId(String itemId) {
        return ItemsRepository.getInstance().getAccessRecommendationsByItemId(itemId);
    }
}

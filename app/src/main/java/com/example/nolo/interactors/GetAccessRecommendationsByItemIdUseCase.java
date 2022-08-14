package com.example.nolo.interactors;

import com.example.nolo.repositories.item.ItemsRepository;

import java.util.List;

public class GetAccessRecommendationsByItemIdUseCase {
    public static List<String> getAccessRecommendationsByItemId(String itemId) {
        return ItemsRepository.getInstance().getItemById(itemId).getRecommendedAccessories();
    }
}

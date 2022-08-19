package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.repositories.item.ItemsRepository;

public class GetItemByIdUseCase {
    /**
     * Get Item entity by item ID
     *
     * @param itemId item ID
     * @return Item entity if itemId exists;
     *         Otherwise null
     */
    public static IItem getItemById(String itemId) {
        return ItemsRepository.getInstance().getItemById(itemId);
    }
}

package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.repositories.item.ItemsRepository;

import java.util.List;

public class GetAllItemsUseCase {
    /**
     * Get all Items
     *
     * @return List of Items
     */
    public static List<IItem> getAllItems() {
        return ItemsRepository.getInstance().getAllItems();
    }
}

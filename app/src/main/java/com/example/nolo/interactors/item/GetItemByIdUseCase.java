package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.repositories.item.ItemsRepository;

public class GetItemByIdUseCase {
    public static IItem getItemById(String itemId) {
        return ItemsRepository.getInstance().getItemById(itemId);
    }
}

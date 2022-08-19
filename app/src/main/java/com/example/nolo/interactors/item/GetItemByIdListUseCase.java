package com.example.nolo.interactors.item;

import com.example.nolo.entities.item.IItem;
import com.example.nolo.repositories.item.ItemsRepository;

import java.util.List;

public class GetItemByIdListUseCase {
    /**
     * Get list of Items by list of item IDs.
     * Convert list of Item IDs to list of Item entities
     *
     * @param itemIds List of item IDs
     * @return List of Item entities
     */
    public static List<IItem> getItemByIdList(List<String> itemIds) {
        return ItemsRepository.getInstance().getItemByIdList(itemIds);
    }
}

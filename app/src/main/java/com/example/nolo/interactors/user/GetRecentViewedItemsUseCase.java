package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.List;

public class GetRecentViewedItemsUseCase {
    public static List<ItemVariant> getRecentViewedItems() {
        return UsersRepository.getInstance().getViewHistory();
    }
}

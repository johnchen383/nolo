package com.example.nolo.interactors;

import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.List;

public class GetRecentViewedItemsUseCase {
    public static List<IItemVariant> getRecentViewedItems() {
        return UsersRepository.getInstance().getViewHistory();
    }
}

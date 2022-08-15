package com.example.nolo.interactors;

import com.example.nolo.entities.item.IItemVariant;
import com.example.nolo.repositories.user.UsersRepository;

public class AddViewedItemUseCase {
    public static void addViewHistory(IItemVariant item) {
        UsersRepository.getInstance().addViewHistory(item);
    }
}

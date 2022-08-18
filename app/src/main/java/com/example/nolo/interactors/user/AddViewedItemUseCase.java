package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.repositories.user.UsersRepository;

public class AddViewedItemUseCase {
    /**
     * records a selected itemvariant as being viewed in the view history of the current user, and pushes
     * to firebase
     * @param item
     */
    public static void addViewHistory(IItemVariant item) {
        UsersRepository.getInstance().addViewHistory(item);
    }
}

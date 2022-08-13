package com.example.nolo.interactors;

import com.example.nolo.repositories.user.UsersRepository;

public class AddViewedItemUseCase {
    public static void addViewHistory(String itemId) {
        UsersRepository.getInstance().addViewHistory(itemId);
    }
}

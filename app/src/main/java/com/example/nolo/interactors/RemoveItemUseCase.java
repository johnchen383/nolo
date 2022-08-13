package com.example.nolo.interactors;

import com.example.nolo.repositories.user.UsersRepository;

public class RemoveItemUseCase {
    public static void removeCart(String itemId) {
        UsersRepository.getInstance().removeCart(itemId);
    }
}

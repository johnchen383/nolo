package com.example.nolo.interactors;

import com.example.nolo.repositories.user.UsersRepository;

public class AddCartItemUseCase {
    public static void addCart(String itemId) {
        UsersRepository.getInstance().addCart(itemId);
    }
}

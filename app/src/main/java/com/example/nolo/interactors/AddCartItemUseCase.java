package com.example.nolo.interactors;

import com.example.nolo.entities.item.IPurchasable;
import com.example.nolo.repositories.user.UsersRepository;

public class AddCartItemUseCase {
    public static void addCart(IPurchasable cartItem) {
        UsersRepository.getInstance().addCart(cartItem);
    }
}

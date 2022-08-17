package com.example.nolo.interactors;

import com.example.nolo.entities.item.IPurchasable;
import com.example.nolo.repositories.user.UsersRepository;

public class AddCartItemUseCase {
    /**
     * Adds cart item to cart of current user, and pushes to firebase
     * @param cartItem
     */
    public static void addCart(IPurchasable cartItem) {
        UsersRepository.getInstance().addCart(cartItem);
    }
}

package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.repositories.user.UsersRepository;

public class AddCartItemUseCase {
    /**
     * Adds cart item to cart of current user, and pushes to firebase
     *
     * @param cartItem
     */
    public static void addCart(IItemVariant cartItem, int quantity) {
        UsersRepository.getInstance().addCart(cartItem, quantity);
    }
}

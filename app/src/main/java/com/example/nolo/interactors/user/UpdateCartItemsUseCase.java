package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.List;

public class UpdateCartItemsUseCase {
    /**
     * Update the user's cart with the new cart list and push to firebase
     *
     * @param cartItems New cart list
     */
    public static void updateCartItems(List<Purchasable> cartItems) {
        UsersRepository.getInstance().updateCart(cartItems);
    }
}

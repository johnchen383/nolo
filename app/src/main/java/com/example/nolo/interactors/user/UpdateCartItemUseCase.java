package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.List;

public class UpdateCartItemUseCase {
    public static void updateCartItem(List<Purchasable> cartItems) {
        UsersRepository.getInstance().updateCart(cartItems);
    }
}

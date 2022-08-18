package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.repositories.user.UsersRepository;

public class RemoveItemUseCase {
    public static void removeCart(IPurchasable cartItem) {
        UsersRepository.getInstance().removeCart(cartItem);
    }
}
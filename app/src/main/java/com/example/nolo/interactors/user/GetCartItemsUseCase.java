package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.List;

public class GetCartItemsUseCase {
    public static List<IPurchasable> getCartItems() {
        return UsersRepository.getInstance().getCart();
    }
}

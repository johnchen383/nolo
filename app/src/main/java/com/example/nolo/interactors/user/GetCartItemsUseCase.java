package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.List;

public class GetCartItemsUseCase {
    public static List<Purchasable> getCartItems() {
        return UsersRepository.getInstance().getCart();
    }
}

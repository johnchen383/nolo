package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.List;

public class GetPurchasedItemsUseCase {
    public static List<Purchasable> getPurchaseHistory() {
        return UsersRepository.getInstance().getPurchaseHistory();
    }
}

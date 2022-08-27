package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.List;

public class AddPurchasedItemsUseCase {
    /**
     * Add purchased items into purchase history at the top and also save it into Firebase
     *
     * @param purchasedItem purchased items
     */
    public static void addPurchaseHistory(List<Purchasable> purchasedItem) {
        UsersRepository.getInstance().addPurchaseHistory(purchasedItem);
    }
}

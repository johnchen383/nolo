package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.enums.PurchaseStatus;

import java.util.List;

public class MoveCartItemsToPurchasedItemsUseCase {
    /**
     * Move cart to purchase history and set status to inTransit
     */
    public static void moveCartItemsToPurchasedItems() {
        List<Purchasable> cartItems = GetCartItemsUseCase.getCartItems();

        // Set status to inTransit to all items
        cartItems.forEach(item -> item.setStatus(PurchaseStatus.inTransit));

        AddPurchasedItemsUseCase.addPurchaseHistory(cartItems);
        ClearCartItemUseCase.clearCartItem();
    }
}

package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.interactors.user.GetCurrentUserUseCase;
import com.example.nolo.interactors.user.MoveCartItemsToPurchasedItemsUseCase;
import com.example.nolo.interactors.user.UpdateCartItemsUseCase;

import java.util.List;

public class CartViewModel extends ViewModel implements ICartViewModel {
    @Override
    public List<Purchasable> getUserCart() {
        return GetCurrentUserUseCase.getCurrentUser().getCart();
    }

    @Override
    public void updateCartItem(List<Purchasable> cartItems) {
        UpdateCartItemsUseCase.updateCartItems(cartItems);
    }

    @Override
    public void moveCartToPurchaseHistory() {
        MoveCartItemsToPurchasedItemsUseCase.moveCartItemsToPurchasedItems();
    }

    @Override
    public boolean checkCartEmpty() {
        return getUserCart().isEmpty();
    }
}

package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.enums.PurchaseStatus;
import com.example.nolo.interactors.user.AddPurchasedItemsUseCase;
import com.example.nolo.interactors.user.ClearCartItemUseCase;
import com.example.nolo.interactors.user.GetCurrentUserUseCase;
import com.example.nolo.interactors.user.UpdateCartItemUseCase;

import java.util.List;

public class CartViewModel extends ViewModel implements ICartViewModel {
    @Override
    public List<Purchasable> getUserCart() {
        return GetCurrentUserUseCase.getCurrentUser().getCart();
    }

    @Override
    public void updateCartItem(List<Purchasable> cartItems) {
        UpdateCartItemUseCase.updateCartItem(cartItems);
    }

    @Override
    public void addPurchaseHistory() {
        List<Purchasable> cartItems = getUserCart();
        cartItems.forEach(o -> {
            o.setStatus(PurchaseStatus.inTransit);
        });
        ClearCartItemUseCase.clearCartItem();
        AddPurchasedItemsUseCase.addPurchaseHistory(cartItems);
    }

}

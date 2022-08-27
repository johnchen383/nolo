package com.example.nolo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.enums.PurchaseStatus;
import com.example.nolo.interactors.user.AddPurchasedItemsUseCase;
import com.example.nolo.interactors.user.GetCurrentUserUseCase;
import com.example.nolo.interactors.user.UpdateCartItemUseCase;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.List;

public class CartViewModel extends ViewModel {
    public List<Purchasable> getUserCart() {
        return GetCurrentUserUseCase.getCurrentUser().getCart();
    }

    public void updateCartItem(List<Purchasable> cartItems) {
        UpdateCartItemUseCase.updateCartItem(cartItems);
    }

    public void addPurchaseHistory() {
        List<Purchasable> cartItems = getUserCart();
        cartItems.forEach(o -> {
            o.setStatus(PurchaseStatus.inTransit);
        });
        AddPurchasedItemsUseCase.addPurchaseHistory(cartItems);
    }

}

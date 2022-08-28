package com.example.nolo.viewmodels;

import com.example.nolo.entities.item.purchasable.Purchasable;

import java.util.List;

public interface ICartViewModel {
    List<Purchasable> getUserCart();
    void updateCartItem(List<Purchasable> cartItems);
    void moveCartToPurchaseHistory();
    boolean checkCartEmpty();
}

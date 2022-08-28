package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.enums.PurchaseStatus;
import com.example.nolo.interactors.user.GetCurrentUserUseCase;

import java.util.List;
import java.util.stream.Collectors;

public class WishlistViewModel extends ViewModel {
    public List<Purchasable> getUserWishlist() {
        return GetCurrentUserUseCase.getCurrentUser().getCart();
    }
}

package com.example.nolo.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.interactors.user.GetWishlistItemsUseCase;
import com.example.nolo.interactors.user.RemoveWishlistItemsUseCase;

import java.util.List;

public class WishlistViewModel extends ViewModel implements IWishlistViewModel {
    @Override
    public List<ItemVariant> getUserWishlist() {
        return GetWishlistItemsUseCase.getWishlist();
    }

    @Override
    public void removeWishlistItem(IItemVariant wishlistItem) {
        RemoveWishlistItemsUseCase.removeWishlistItem(wishlistItem);
    }

    @Override
    public boolean checkWishlistEmpty() {
        return getUserWishlist().isEmpty();
    }
}

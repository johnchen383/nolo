package com.example.nolo.viewmodels;

import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;

import java.util.List;

public interface IWishlistViewModel {
    List<ItemVariant> getUserWishlist();
    void removeWishlistItem(IItemVariant wishlistItem);
}

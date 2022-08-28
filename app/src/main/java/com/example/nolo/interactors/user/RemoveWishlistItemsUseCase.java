package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.repositories.user.UsersRepository;

public class RemoveWishlistItemsUseCase {
    /**
     * Remove item from the user's wishlist
     *
     * @param wishlistItem The item to be removed
     */
    public static void removeWishlistItem(IItemVariant wishlistItem) {
        UsersRepository.getInstance().removeWishlist(wishlistItem);
    }
}

package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.repositories.user.UsersRepository;

public class UpdateWishlistItemsUseCase {
    /**
     * Remove item from the user's wishlist
     *
     * @param wishlistItem The item to be removed
     */
    public static void updateWishlistItems(ItemVariant wishlistItem) {
        UsersRepository.getInstance().removeWishlist(wishlistItem);
    }
}

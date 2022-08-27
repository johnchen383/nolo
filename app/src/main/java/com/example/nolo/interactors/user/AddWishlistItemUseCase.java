package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.repositories.user.UsersRepository;

public class AddWishlistItemUseCase {
    /**
     * Adds wishlist item to wishlist of current user, and pushes to firebase
     *
     * @param wishlistItem
     */
    public static void addWishlist(IItemVariant wishlistItem) {
        UsersRepository.getInstance().addWishlist(wishlistItem);
    }
}

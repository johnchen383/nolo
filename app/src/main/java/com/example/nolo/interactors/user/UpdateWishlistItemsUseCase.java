package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.List;

public class UpdateWishlistItemsUseCase {
    /**
     * Update the user's wishlist with the new wishlist
     *
     * @param wishlistItems New wishlist
     */
    public static void updateWishlistItems(List<ItemVariant> wishlistItems) {
        UsersRepository.getInstance().updateWishlist(wishlistItems);
    }
}

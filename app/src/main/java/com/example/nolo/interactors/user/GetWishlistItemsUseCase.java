package com.example.nolo.interactors.user;

import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.repositories.user.UsersRepository;

import java.util.List;

public class GetWishlistItemsUseCase {
    public static List<ItemVariant> getWishlist() {
        return UsersRepository.getInstance().getWishlist();
    }
}

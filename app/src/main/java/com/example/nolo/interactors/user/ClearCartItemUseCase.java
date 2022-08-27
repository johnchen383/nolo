package com.example.nolo.interactors.user;

import java.util.ArrayList;

public class ClearCartItemUseCase {
    /**
     * Clear cart and also clear it in Firebase
     */
    public static void clearCartItem() {
        UpdateCartItemUseCase.updateCartItem(new ArrayList<>());
    }
}

package com.example.nolo.entities.user;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;

import java.util.List;

public interface IUser {
    String getUserAuthUid();
    void setUserAuthUid(String userAuthUid);
    String getEmail();
    void setEmail(String email);
    List<ItemVariant> getViewHistory();
    void addViewHistory(IItemVariant item);
    List<ItemVariant> getWishlist();
    void addWishlist(IItemVariant item);
    void updateWishlist(List<ItemVariant> items);
    List<Purchasable> getPurchaseHistory();
    void addPurchaseHistory(List<Purchasable> purchasedItem);
    List<Purchasable> getCart();
    void addCart(IItemVariant cartItem, int quantity);
    void updateCart(List<Purchasable> cartItems);
    boolean isFieldNameValid(String fieldName);
}

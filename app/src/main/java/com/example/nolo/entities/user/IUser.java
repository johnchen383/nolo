package com.example.nolo.entities.user;

import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.variant.ItemVariant;

import java.util.List;

public interface IUser {
    void setUserAuthUid(String userAuthUid);
    String getUserAuthUid();
    void setEmail(String email);
    String getEmail();
    List<ItemVariant> getViewHistory();
    void addViewHistory(IItemVariant item);
    List<Purchasable> getCart();
    void addCart(IPurchasable cartItem);
    void removeCart(IPurchasable cartItem);
    boolean isFieldNameValid(String fieldName);
}

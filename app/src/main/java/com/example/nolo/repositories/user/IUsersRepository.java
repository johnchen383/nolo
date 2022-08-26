package com.example.nolo.repositories.user;

import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.item.purchasable.Purchasable;
import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.variant.ItemVariant;
import com.example.nolo.entities.user.IUser;

import java.util.List;
import java.util.function.Consumer;

public interface IUsersRepository {
    void loadUsers(Consumer<Class<?>> onLoadedRepository);
    IUser getCurrentUser();
    void signUp(Consumer<String> onUserSignedUp, String email, String password);
    void logIn(Consumer<String> onUserLoggedIn, String email, String password);
    void logOut();
    List<ItemVariant> getViewHistory();
    void addViewHistory(IItemVariant item);
    List<Purchasable> getPurchaseHistory();
    void addPurchaseHistory(List<Purchasable> purchasedItem);
    List<Purchasable> getCart();
    void addCart(IPurchasable cartItem);
    void updateCart(List<Purchasable> cartItems);
}

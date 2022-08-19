package com.example.nolo.repositories.user;

import com.example.nolo.entities.item.variant.IItemVariant;
import com.example.nolo.entities.item.purchasable.IPurchasable;
import com.example.nolo.entities.user.IUser;

import java.util.List;
import java.util.function.Consumer;

public interface IUsersRepository {
    void loadUsers(Consumer<Class<?>> onLoadedRepository);
    IUser getCurrentUser();
    void signUp(Consumer<String> onUserSignedUp, String email, String password);
    void logIn(Consumer<String> onUserLoggedIn, String email, String password);
    void logOut();
    void addViewHistory(IItemVariant item);
    List<IItemVariant> getViewHistory();
    void addCart(IPurchasable cartItem);
    void removeCart(IPurchasable cartItem);
    List<IPurchasable> getCart();
}
